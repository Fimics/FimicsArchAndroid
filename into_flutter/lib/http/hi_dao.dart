import 'package:intoflutter/http/hi_error.dart';
import 'package:intoflutter/http/hi_http.dart';
import 'package:intoflutter/http/request/base_request.dart';
import 'package:intoflutter/http/request/recommend_request.dart';
import 'package:intoflutter/model/common_model.dart';
import 'package:intoflutter/page/base_page.dart';

class HiDao {
  HiDao._();

  static HiDao _instance;

  static HiDao getInstance() {
    if (_instance == null) {
      _instance = HiDao._();
    }
    return _instance;
  }

  var _cached = Map();
  static const RECOMMENDS_SAVE_KEY = "RECOMMENDS_SAVE_KEY";
  static const FAVORITES_SAVE_KEY = "FAVORITES_SAVE_KEY";

  bool containCached(String key) {
    return _cached.containsKey(key);
  }

  ///推荐接口
  void recommends(
      BaseState state, Function(CommonModel) success, Function(HiError) error,
      {int pageIndex = 1, int pageSize = 10, bool needCached = false}) async {
    _send(RecommendRequest(), state, success, error,
        pageIndex: pageIndex, pageSize: pageSize, needCached: needCached);
  }

  void _send(BaseRequest request, BaseState state,
      Function(CommonModel) success, Function(HiError) error,
      {int pageIndex = 1, int pageSize = 10, bool needCached = false}) async {
    var saveKey =
        request is RecommendRequest ? RECOMMENDS_SAVE_KEY : FAVORITES_SAVE_KEY;
    if (pageIndex == 1 && needCached && containCached(saveKey)) {
      success(_cached[saveKey]);
    }
    request.add("pageIndex", pageIndex);
    request.add("pageSize", pageSize);
    try {
      var result = await HiHttp.getInstance().fire(request);
      var model = CommonModel.fromJson(result);
      if (pageIndex == 1) {
        _cached[saveKey] = model;
      }
      if (state != null && state.isDispose) return;
      success(model);
    } catch (e) {
      if (state != null && state.isDispose) return;
      error(e.message);
      print('hi-dao:${e.toString()}');
    }
  }
}
