package org.pjj.gof23.factory02;

/**
 * @author PengJiaJun
 * @Date 2022/08/05 00:20
 */
public class AudiFactory implements CarFactoryInterface {
    @Override
    public Car createCar() {
        return new Audi();
    }
}
