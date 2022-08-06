package org.pjj.gof23.decorator;

/**
 * 具体装饰角色直接实现 ICar接口 也就是 被装饰对象实现的接口, 然后传入一个 真实的车对象. 然后重写真实车对象的方法(保留原有方法, 然后再加入一些自己的元素)
 *
 * @author PengJiaJun
 * @Date 2022/08/06 15:43
 */
public class AICar implements ICar {

    protected ICar car;

    public AICar(ICar car) {
        super();
        this.car = car;
    }

    @Override
    public void move() {
        car.move();
        System.out.println("AI自动驾驶");
    }
}
