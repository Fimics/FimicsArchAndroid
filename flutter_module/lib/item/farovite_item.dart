import 'package:flutter/material.dart';
import 'package:flutter_module/core/hi_flutter_bridge.dart';
import 'package:flutter_module/model/goods_model.dart';

class FavoriteItem extends StatelessWidget {
  final GoodsModel item;
  final int index;

  const FavoriteItem({Key key, this.item, this.index}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        HiFlutterBridge.getInstance()
            .goToNative({"action": "goToDetail", "goodsId": item.goodsId});
      },
      child: Card(
        //固定子widget的高度为容器的高度
        child: IntrinsicHeight(
          child: Padding(
            padding: EdgeInsets.all(10),
            child: Row(
              children: <Widget>[_itemImage(), _itemText()],
            ),
          ),
        ),
      ),
    );
  }

  _itemImage() {
    return Container(
      child: PhysicalModel(
        color: Colors.transparent,
        clipBehavior: Clip.antiAlias,
        borderRadius: BorderRadius.circular(2),
        child: Image.network(item.sliderImage),
      ),
      height: 128,
      width: 128,
      margin: EdgeInsets.only(right: 10),
    );
  }

  _itemText() {
    //让侧容器填满剩余的横向空间
    return Expanded(
        child: Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text(
          item.goodsName ?? "",
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(fontSize: 14, color: Colors.black87),
        ),
        _infoText()
      ],
    ));
  }

  _infoText() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Row(
          children: <Widget>[
            Text(
              "￥",
              style: TextStyle(fontSize: 10, color: Colors.redAccent),
            ),
            Padding(
                padding: EdgeInsets.only(right: 5),
                child: Text(
                  item.groupPrice ?? "",
                  style: TextStyle(fontSize: 18, color: Colors.redAccent),
                )),
            Text(
              item.completedNumText ?? "",
              style: TextStyle(fontSize: 12, color: Colors.grey),
            )
          ],
        ),
        GestureDetector(
          child: Container(
            padding: EdgeInsets.only(left: 20),
            child: Icon(
              Icons.more_horiz,
              size: 20,
              color: Colors.black26,
            ),
          ),
          onTap: () {
            //todo
            print("click more");
          },
        )
      ],
    );
  }
}
