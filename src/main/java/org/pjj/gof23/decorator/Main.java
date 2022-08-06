package org.pjj.gof23.decorator;

/**
 * @author PengJiaJun
 * @Date 2022/08/06 15:36
 */
public class Main {
    public static void main(String[] args) {
        Car car = new Car();//普通的车
        car.move();//陆地上跑!

        System.out.println("================");

        FlyCar flyCar = new FlyCar(new Car());//使用flyCar装饰普通的车, 得到新的车, 既有普通的车的效果, 也有本类的效果
        flyCar.move();//陆地上跑!、天上飞!

        System.out.println("================");

        WaterCar waterCar = new WaterCar(new FlyCar(new Car()));//使用waterCar装饰一个flyCar车, 增强车的功能(在该车原有的功能上)加上本类的效果 水里游泳
        waterCar.move();//陆地上跑!、天上飞!、水里游!

        System.out.println("================");

        AICar aiCar = new AICar(new FlyCar(new Car()));
        aiCar.move();//陆地上跑!、天上飞!、AI自动驾驶
    }
}
