import 'package:flutter/material.dart';

class TabPage extends StatefulWidget {
  final String category;

  const TabPage({Key key, this.category}) : super(key: key);

  @override
  _TabPageState createState() => _TabPageState();
}

class _TabPageState extends State<TabPage> with AutomaticKeepAliveClientMixin{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(color: Colors.orangeAccent),
        child: Center(
          child: Text(
            widget.category,
            style: TextStyle(color: Colors.white, fontSize: 20),
          ),
        ),
      ),
    );
  }

  @override
  bool get wantKeepAlive => true;
}
