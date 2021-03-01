package org.devio.design.pattern.decorator;

/**
 * 被装饰者
 */
public class Panda implements Animal {
    @Override
    public void eat() {
        System.out.println("什么都没有，不知道吃什么");
    }
}
