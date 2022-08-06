package org.pjj.gof23.decorator;

/**
 * 实现了 车接口 的普通的车
 *
 * 对应装饰器模式中的 真实对象 (被装饰的对象)
 * @author PengJiaJun
 * @Date 2022/08/06 15:15
 */
public class Car implements ICar {
    @Override
    public void move() {
        System.out.println("陆地上跑!");
    }
}
