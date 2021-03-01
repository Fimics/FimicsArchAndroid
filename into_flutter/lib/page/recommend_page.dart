import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:intoflutter/http/hi_dao.dart';
import 'package:intoflutter/http/hi_error.dart';
import 'package:intoflutter/item/recommend_item.dart';
import 'package:intoflutter/model/common_model.dart';
import 'package:intoflutter/model/goods_model.dart';
import 'package:intoflutter/page/base_page.dart';
import 'package:intoflutter/widget/loading_container.dart';

class RecommendPage extends StatefulWidget {
  @override
  _RecommendPageState createState() => _RecommendPageState();
}

class _RecommendPageState extends BaseState<RecommendPage>
    with AutomaticKeepAliveClientMixin {
  int pageIndex = 1;
  List<GoodsModel> goodsModels;
  bool _loading = true;
  ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    _loadData(needCached: true);
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        _loadData(loadMore: true);
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return LoadingContainer(
      isLoading: _loading,
      child: RefreshIndicator(
        color: Color(0xffd44949),
        onRefresh: _handleRefresh,
        child: MediaQuery.removePadding(
            removeTop: true,
            context: context,
            child: StaggeredGridView.countBuilder(
                controller: _scrollController,
                crossAxisCount: 4,
                itemCount: goodsModels?.length ?? 0,
                itemBuilder: (BuildContext context, int index) => RecommendItem(
                      index: index,
                      item: goodsModels[index],
                    ),
                staggeredTileBuilder: (int index) => StaggeredTile.fit(2))),
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
    HiDao.getInstance().recommends(this, (CommonModel model) {
      setState(() {
        _loading = false;
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
    }, pageIndex: pageIndex, pageSize: 10, needCached: needCached);
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  bool get wantKeepAlive => true;
}
