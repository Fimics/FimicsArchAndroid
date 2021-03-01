import 'package:flutter/material.dart';

import 'page/favorite_page.dart';
import 'page/hi_native_page.dart';
import 'page/recommend_page.dart';

//至少要有主入口
void main() => runApp(MyApp(FavoritePage()));
//注册多个dart module
@pragma(
    'vm:entry-point') // say the release that this function must alos in the project used and it must not deleted
void recommend() => runApp(MyApp(RecommendPage()));
//注册多个dart module
@pragma(
    'vm:entry-point')
void nativeView() => runApp(MyApp(HiNativePage()));

class MyApp extends StatelessWidget {
  final Widget page;

  MyApp(this.page);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Android架构师',
        home: Scaffold(
          body: page,
        ));
  }
}
