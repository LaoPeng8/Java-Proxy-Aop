package org.pjj.gof23.decorator;

/**
 * 具体的装饰角色
 * @author PengJiaJun
 * @Date 2022/08/06 15:38
 */
public class WaterCar extends SuperCar {
    public WaterCar(ICar car) {
        super(car);
    }

    @Override
    public void move() {
        super.move();
        System.out.println("水里游!");
    }
}
