import 'package:flutter/material.dart';

abstract class BaseState<T extends StatefulWidget> extends State<T> {
  bool isDispose = false;
  bool isInit = false;

  @override
  void initState() {
    isInit = true;
    super.initState();
  }

  @override
  void dispose() {
    isDispose = true;
    super.dispose();
  }
}
