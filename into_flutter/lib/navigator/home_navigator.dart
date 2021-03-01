import 'package:flutter/material.dart';
import 'package:intoflutter/page/demo_page.dart';
import 'package:intoflutter/page/home_page.dart';
import 'package:intoflutter/page/recommend_page.dart';

class HomeNavigator extends StatefulWidget {
  @override
  _HomeNavigatorState createState() => _HomeNavigatorState();
}

class _HomeNavigatorState extends State<HomeNavigator> {
  final _defaultColor = Colors.grey;
  final _activeColor = Colors.blue;
  int _currentIndex = 0;
  final PageController _controller = PageController(initialPage: 0);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        onPageChanged: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
        controller: _controller,
        children: [
          HomePage(),
          DemoPage(
            title: '收藏',
          ),
          DemoPage(
            title: '分类',
          ),
          RecommendPage(),
          DemoPage(
            title: '我的',
          ),
        ],
        physics: NeverScrollableScrollPhysics(),
      ),
      bottomNavigationBar: BottomNavigationBar(
          currentIndex: _currentIndex,
          onTap: (index) {
            _controller.jumpToPage(index);
            setState(() {
              _currentIndex = index;
            });
          },
          type: BottomNavigationBarType.fixed,
          items: [
            _bottomItem('首页', Icons.home, 0),
            _bottomItem('收藏', Icons.favorite, 1),
            _bottomItem('分类', Icons.category, 2),
            _bottomItem('推荐', Icons.save, 3),
            _bottomItem('我的', Icons.account_circle, 4)
          ]),
    );
  }

  _bottomItem(String title, IconData icon, int index) {
    return BottomNavigationBarItem(
        icon: Icon(
          icon,
          color: _defaultColor,
        ),
        activeIcon: Icon(
          icon,
          color: _activeColor,
        ),
        title: Text(
          title,
          style: TextStyle(
              color: _currentIndex != index ? _defaultColor : _activeColor),
        ));
  }
}
