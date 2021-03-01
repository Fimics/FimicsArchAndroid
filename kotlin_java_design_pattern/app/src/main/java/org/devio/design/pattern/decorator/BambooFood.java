package org.devio.design.pattern.decorator;

/**
 * 具体装饰
 */
public class BambooFood extends Food {
    public BambooFood(Animal animal) {
        super(animal);
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("可以吃竹子");
    }
}
