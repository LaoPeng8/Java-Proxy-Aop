package org.pjj.gof23.decorator;

/**
 * 具体装饰角色
 * @author PengJiaJun
 * @Date 2022/08/06 15:32
 */
public class FlyCar extends SuperCar {
    public FlyCar(ICar car) {
        super(car);
    }

    /**
     * 核心方法, 根据构造器转入的 car , 在 car 原有的功能上, 增加本类的功能, 即 飞.
     */
    @Override
    public void move() {
        super.move();
        System.out.println("天上飞!");
    }
}
