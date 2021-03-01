class NeedLogin implements HiError {
  @override
  int get code => 401;

  @override
  String get message => "请先登录";
}

class NeedAuth implements HiError {
  @override
  int get code => 403;

  @override
  String get message => "非法的访问";
}

class HiError {
  final int code;
  final String message;

  HiError(this.code, this.message);
}
