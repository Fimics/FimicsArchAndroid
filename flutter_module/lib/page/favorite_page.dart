import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/hi_flutter_bridge.dart';
import 'package:flutter_module/http/hi_dao.dart';
import 'package:flutter_module/http/hi_error.dart';
import 'package:flutter_module/item/farovite_item.dart';
import 'package:flutter_module/model/common_model.dart';
import 'package:flutter_module/model/goods_model.dart';
import 'package:flutter_module/page/base_page.dart';
import 'package:flutter_module/widget/loading_container.dart';
import 'package:flutter_module/widget/login_widget.dart';

class FavoritePage extends StatefulWidget {
  @override
  _FavoritePageState createState() => _FavoritePageState();
}

class _FavoritePageState extends BaseState<FavoritePage>
    with AutomaticKeepAliveClientMixin {
  int pageIndex = 1;
  bool _loading = true;
  bool _needLogin = false;
  List<GoodsModel> goodsModels;
  ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    _loadData(needCached: false);

    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        _loadData(loadMore: true);
      }
    });
    HiFlutterBridge.getInstance().register("onRefresh", (MethodCall call) {
      _loadData();
    });
    super.initState();
  }

  @override
  void dispose() {
    HiFlutterBridge.getInstance().unRegister("onRefresh");
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return LoadingContainer(
      isLoading: _loading,
      child: !_needLogin
          ? _content
          : LoginWidget(
              loginButtonClick: () {
                HiFlutterBridge.getInstance()
                    .goToNative({"action": "goToLogin"});
              },
            ),
    );
  }

  Future<Null> _handleRefresh() async {
    _loadData();
    return null;
  }

  void _loadData({loadMore = false, needCached = false}) {
    if (loadMore) {
      pageIndex++;
    } else {
      pageIndex = 1;
    }

    HiDao.getInstance().favorites(this, (CommonModel model) {
      setState(() {
        _loading = false;
        _needLogin = false;
        List<GoodsModel> items = model.list;
        if (goodsModels != null && pageIndex != 1) {
          goodsModels.addAll(items);
        } else {
          goodsModels = items;
        }
      });
    }, (HiError error) {
      setState(() {
        _loading = false;
      });
      if (error is NeedLogin) {
        setState(() {
          _needLogin = true;
        });
      }
    }, pageIndex: pageIndex, needCached: needCached);
  }

  @override
  bool get wantKeepAlive => true;

  Widget get _content {
    return RefreshIndicator(
      color: Color(0xffd44949),
      onRefresh: _handleRefresh,
      child: MediaQuery.removePadding(
          removeTop: true,
          context: context,
          child: ListView.builder(
            controller: _scrollController,
            itemCount: goodsModels?.length ?? 0,
            padding: EdgeInsets.only(bottom: 60),
            itemBuilder: (BuildContext context, int index) => FavoriteItem(
              index: index,
              item: goodsModels[index],
            ),
          )),
    );
  }
}
