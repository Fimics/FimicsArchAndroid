import 'package:flutter/material.dart';
import 'package:flutter_module/core/hi_flutter_bridge.dart';
import 'package:flutter_module/native/hi_image_view.dart';
import 'package:flutter_module/native/hi_image_view_controller.dart';

class HiNativePage extends StatefulWidget {
  @override
  _HiNativePageState createState() => _HiNativePageState();
}

class _HiNativePageState extends State<HiNativePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: BackButton(
          onPressed: () {
            HiFlutterBridge.getInstance().onBack(null);
          },
        ),
        title: Text('Native View'),
      ),
      body: Column(
        children: [
          Text('Flutter嵌入Native组件的实现原理与应用'),
          SizedBox(
            height: 300,
            child: HiImageView(
              url: "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
              onViewCreated: _onViewCreated,
            ),
          )
        ],
      ),
    );
  }

  void _onViewCreated(HiImageViewController controller) {
    Future.delayed(Duration(milliseconds: 3000), () {
      controller
          .setUrl("https://www.devio.org/img/beauty_camera/beauty_camera7.jpg");
    });
  }
}
