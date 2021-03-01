package org.devio.design.pattern.decorator;

/**
 * 具体装饰
 */
public class CarrotFood extends Food {
    public CarrotFood(Animal animal) {
        super(animal);
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("可以吃胡萝卜");
    }
}
