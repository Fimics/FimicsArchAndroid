class GoodsModel {
  String goodsId;
  String categoryId;
  bool hot;
  List<SliderImages> sliderImages;
  String marketPrice;
  String groupPrice;
  String completedNumText;
  String goodsName;
  String tags;
  Null joinedAvatars;
  String createTime;
  String sliderImage;

  GoodsModel(
      {this.goodsId,
      this.categoryId,
      this.hot,
      this.sliderImages,
      this.marketPrice,
      this.groupPrice,
      this.completedNumText,
      this.goodsName,
      this.tags,
      this.joinedAvatars,
      this.createTime,
      this.sliderImage});

  GoodsModel.fromJson(Map<String, dynamic> json) {
    goodsId = json['goodsId'];
    categoryId = json['categoryId'];
    hot = json['hot'];
    if (json['sliderImages'] != null) {
      sliderImages = new List<SliderImages>();
      json['sliderImages'].forEach((v) {
        sliderImages.add(new SliderImages.fromJson(v));
      });
    }
    marketPrice = json['marketPrice'];
    groupPrice = json['groupPrice'];
    completedNumText = json['completedNumText'];
    goodsName = json['goodsName'];
    tags = json['tags'];
    joinedAvatars = json['joinedAvatars'];
    createTime = json['createTime'];
    sliderImage = json['sliderImage'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['goodsId'] = this.goodsId;
    data['categoryId'] = this.categoryId;
    data['hot'] = this.hot;
    if (this.sliderImages != null) {
      data['sliderImages'] = this.sliderImages.map((v) => v.toJson()).toList();
    }
    data['marketPrice'] = this.marketPrice;
    data['groupPrice'] = this.groupPrice;
    data['completedNumText'] = this.completedNumText;
    data['goodsName'] = this.goodsName;
    data['tags'] = this.tags;
    data['joinedAvatars'] = this.joinedAvatars;
    data['createTime'] = this.createTime;
    data['sliderImage'] = this.sliderImage;
    return data;
  }
}

class SliderImages {
  String url;
  int type;

  SliderImages({this.url, this.type});

  SliderImages.fromJson(Map<String, dynamic> json) {
    url = json['url'];
    type = json['type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['url'] = this.url;
    data['type'] = this.type;
    return data;
  }
}
