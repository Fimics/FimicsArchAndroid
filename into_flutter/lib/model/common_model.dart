
import 'goods_model.dart';

class CommonModel {
  int total;
  List<GoodsModel> list;

  CommonModel({this.total, this.list});

  CommonModel.fromJson(Map<String, dynamic> json) {
    total = json['total'];
    if (json['list'] != null) {
      list = new List<GoodsModel>();
      json['list'].forEach((v) {
        list.add(new GoodsModel.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['total'] = this.total;
    if (this.list != null) {
      data['list'] = this.list.map((v) => v.toJson()).toList();
    }
    return data;
  }
}
