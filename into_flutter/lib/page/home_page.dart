import 'package:flutter/material.dart';
import 'package:intoflutter/page/tab_page.dart';
import 'package:underline_indicator/underline_indicator.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with TickerProviderStateMixin {
  TabController _controller;
  static const tabs = [
    '热门',
    '女装',
    '鞋包',
    '百货',
    '手机',
    '食品',
    '男装',
    '数码',
    '手机',
  ];

  @override
  void initState() {
    super.initState();
    _controller = TabController(length: tabs.length, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Container(
            color: Colors.white,
            padding: EdgeInsets.only(top: 30),
            child: TabBar(
              controller: _controller,
              isScrollable: true,
              labelColor: Colors.black,
              indicator: UnderlineIndicator(
                  strokeCap: StrokeCap.round,
                  borderSide: BorderSide(color: Colors.blue, width: 3),
                  insets: EdgeInsets.only(bottom: 2)),
              tabs: tabs.map<Tab>((String tab) {
                return Tab(
                  text: tab,
                );
              }).toList(),
            ),
          ),
          Flexible(
            child: TabBarView(
              controller: _controller,
              children: tabs.map((String tab) {
                return TabPage(
                  category: tab,
                );
              }).toList(),
            ),
          )
        ],
      ),
    );
  }
}
