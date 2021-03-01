import 'package:flutter/material.dart';

class DemoPage extends StatefulWidget {
  final String title;

  const DemoPage({Key key, this.title}) : super(key: key);

  @override
  _DemoPageState createState() => _DemoPageState();
}

class _DemoPageState extends State<DemoPage> {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(color: Colors.redAccent),
      child: Center(
        child: Text(
          widget.title,
          style: TextStyle(color: Colors.white, fontSize: 20),
        ),
      ),
    );
  }
}
