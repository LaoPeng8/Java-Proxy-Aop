package org.pjj.gof23.decorator;

/**
 * 装饰角色
 *
 * 我个人感觉 其实完全可以不要这个类(具体可以看AICar.java), 只是为了规范这个 装饰器的规则, 比如 作为车装饰器,必须要有一个car对象
 *
 * @author PengJiaJun
 * @Date 2022/08/06 15:21
 */
public class SuperCar implements ICar {

    protected ICar car;

    public SuperCar(ICar car) {
        super();
        this.car = car;
    }

    @Override
    public void move() {
        car.move();
    }
}
