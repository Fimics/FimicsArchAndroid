package org.devio.design.pattern.decorator;

/**
 * 装饰者组件
 */
public abstract class Food implements Animal {
    Animal animal;

    public Food(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void eat() {
        animal.eat();
    }
}
