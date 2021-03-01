import 'package:flutter/material.dart';

///加载进度条组件
class LoadingContainer extends StatelessWidget {
  final Widget child;
  final bool isLoading;
  final bool cover;

  const LoadingContainer(
      {Key key, this.isLoading, this.cover = false, this.child})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return !cover
        ? !isLoading ? child : _loadingView
        : Stack(
            children: [child, isLoading ? _loadingView : Container()],
          );
  }

  Widget get _loadingView {
    return Center(
      child: CircularProgressIndicator(
        valueColor: AlwaysStoppedAnimation<Color>(Color(0xffd44949)),
      ),
    );
  }
}
