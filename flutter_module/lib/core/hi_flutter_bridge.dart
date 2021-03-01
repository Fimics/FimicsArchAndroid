import 'package:flutter/services.dart';

///Flutter通信框架
class HiFlutterBridge {
  static HiFlutterBridge _instance = new HiFlutterBridge._();
  MethodChannel _bridge = const MethodChannel('HiFlutterBridge');
  var _listeners = {};
  var header;

  HiFlutterBridge._() {
    _bridge.setMethodCallHandler((MethodCall call) {
      String method = call.method;
      if (_listeners[method] != null) {
        return _listeners[method](call);
      }
      return null;
    });
  }

  static HiFlutterBridge getInstance() {
    return _instance;
  }

  register(String method, Function(MethodCall) callBack) {
    _listeners[method] = callBack;
  }

  unRegister(String method) {
    _listeners.remove(method);
  }

  goToNative(Map prams) {
    _bridge.invokeMethod("goToNative", prams);
  }

  onBack(Map prams) {
    _bridge.invokeMethod("onBack", prams);
  }

  Future<Map<String, String>> getHeaderParams() async {
    Map header = await _bridge.invokeMethod('getHeaderParams', {});
    return this.header = Map<String, String>.from(header);
  }

  MethodChannel bridge() {
    return _bridge;
  }
}
