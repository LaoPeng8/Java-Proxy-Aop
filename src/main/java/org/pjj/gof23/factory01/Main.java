package org.pjj.gof23.factory01;

/**
 * 简单工厂模式 也叫 静态工厂模式, 就是工厂类一般使用静态方法, 通过接收的参数的不同来返回不同的对象实例.
 *
 * @author PengJiaJun
 * @Date 2022/08/04 23:06
 */
public class Main {
    public static void main(String[] args) {
        /**
         * 测试没有工厂模式的情况下 创建车对象
         *
         * 很显然没有工厂模式的情况下, 客户(调用者) 与 产品之间 非常耦合
         * 客户既依赖 Audi , 也依赖 Byd 如果 车非常多的情况下, 奔驰宝马啥的几百辆车, 那就相当耦合了, 此时的代码就是 牵一发而动全身
         * 而且
         * 客户只是想到一辆车而已, 这里还需要自己创建, 如果创建一个 Audi车 还需要很多参数, 那岂不是麻烦死
         * 根据面向对象的思想, 客户只要想到一辆车, 不用管这个车是怎么来的, 你提供一个方法, 我直接得到车就ok了
         */
        Car audi = new Audi();
        Car byd = new Byd();
        audi.run();
        byd.run();


        /**
         * 简单工厂情况下 创建车对象
         *
         * 可以看到在简单工厂情况下, 客户只需要依赖 工厂, 并不需要依赖很多车, 而且也不用关心车了怎么创建的,
         * 只需要告诉工厂需要什么车, 工厂就会给你
         *
         * 简单的说 就是达到了解耦的目的
         */
        CarFactory carFactory = new CarFactory();
        Car audi2 = carFactory.createCar("奥迪");
        Car byd2 = carFactory.createCar("比亚迪");
        audi2.run();
        byd2.run();


    }
}
