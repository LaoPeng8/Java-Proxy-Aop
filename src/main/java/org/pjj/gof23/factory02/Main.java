package org.pjj.gof23.factory02;

/**
 * 工厂方法模式
 *
 * 工厂方法模式的出现 就是为了解决 简单工厂模式的 新增产品时(Car), 需要修改工厂代码, 不符合开闭原则, 这个问题而出现的
 * 工厂方法模式将简单工厂模式的 一个工厂, 改为了  一个工厂接口, 与 N 个工厂实现, 即新增产品时不用修改工厂代码
 * 而是 新增一个 工厂接口的实现类 来完成 新产品的生产.
 *
 * 工厂方式模式 其实和 简单工厂模式 就是一样的, 只不过修复了 简单工厂不满足 开闭原则的缺陷
 * 但是我觉得还是简单工厂好一些
 * 简单工厂的优势就是 将客户与产品解耦, 客户只需要依赖一个工厂, 由工厂来解决与产品的耦合,如何创建产品
 * 但是
 * 工厂方式模式虽然不用依赖很多产品, 但是需要依赖很多工厂. 虽然和产品解耦了, 但是又耦合了很多工厂. 感觉和直接依赖产品没什么区别
 *
 * @author PengJiaJun
 * @Date 2022/08/05 00:21
 */
public class Main {
    public static void main(String[] args) {
        CarFactoryInterface audiFactory = new AudiFactory();//奥迪工厂生产奥迪车
        Car audi = audiFactory.createCar();

        CarFactoryInterface bydFactory = new BydFactory();//比亚迪工厂生产比亚迪车
        Car byd = bydFactory.createCar();

        audi.run();
        byd.run();

    }
}
