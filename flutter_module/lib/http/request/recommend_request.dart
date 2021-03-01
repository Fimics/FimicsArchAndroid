import 'package:flutter_module/http/request/base_request.dart';

class RecommendRequest extends BaseRequest {
  @override
  HttpMethod httpMethod() {
    return HttpMethod.GET;
  }

  @override
  bool needLogin() {
    return false;
  }

  @override
  String path() {
    return "/as/goods/recommend";
  }
}
