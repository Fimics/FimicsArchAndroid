import 'base_request.dart';

class FavoriteRequest extends BaseRequest {
  @override
  bool needLogin() {
    return true;
  }

  @override
  String path() {
    return "/as/favorites";
  }

  @override
  HttpMethod httpMethod() {
    return HttpMethod.GET;
  }
}
