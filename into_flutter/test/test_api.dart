import 'package:flutter_test/flutter_test.dart';
import 'package:intoflutter/http/hi_http.dart';
import 'package:intoflutter/http/request/recommend_request.dart';
///API接口测试
void main() {
  testAPI();
}

void testAPI() {
  test("testApi", () async {
    var request = RecommendRequest();
    request.add("pageIndex", 1);
    request.add("pageSize", 10);
    var result = await HiHttp.getInstance().fire(request);
    expect(result['list'], isNotNull);
    print(result);
  });
}
