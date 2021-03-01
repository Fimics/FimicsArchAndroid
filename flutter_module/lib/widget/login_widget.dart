import 'package:flutter/material.dart';

class LoginWidget extends StatefulWidget {
  final void Function() loginButtonClick;

  const LoginWidget({Key key, this.loginButtonClick}) : super(key: key);

  @override
  _LoginWidgetState createState() => _LoginWidgetState();
}

class _LoginWidgetState extends State<LoginWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(top: 150),
      child: Column(
        children: [
          Text('还没有登录哦'),
          Padding(
            padding: EdgeInsets.all(20),
            child: FractionallySizedBox(
              widthFactor: 1,
              child: MaterialButton(
                shape: RoundedRectangleBorder(borderRadius:BorderRadius.circular(6) ),
                height: 45,
                onPressed: widget.loginButtonClick,
                color: Color(0xffDD2F24),
                child: Text(
                  '登录',
                  style: TextStyle(color: Colors.white, fontSize: 16),
                ),
              ),
            ),
          )
        ],
      ),
    );
  }
}
