package org.devio.design.pattern.decorator;

public class Test {
    public static void main(String[] args) {
        //创建被装饰者
        Panda panda = new Panda();
        //熊猫被装饰了竹子 ，可以吃竹子了
        BambooFood bambooFood = new BambooFood(panda);
        //可以吃竹子的熊猫，被装饰了胡萝卜，可以吃胡萝卜了
        CarrotFood carrotFood = new CarrotFood(bambooFood);
        carrotFood.eat();
    }
}
