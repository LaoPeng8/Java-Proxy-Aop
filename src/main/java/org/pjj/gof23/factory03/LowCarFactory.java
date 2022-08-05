package org.pjj.gof23.factory03;

/**
 * 低端车工厂
 * @author PengJiaJun
 * @Date 2022/08/05 17:38
 */
public class LowCarFactory implements CarFactory {
    @Override
    public Engine createEngine() {
        return new LowEngine();
    }

    @Override
    public Seat createSeat() {
        return new LowSeat();
    }

    @Override
    public Tyre createTyre() {
        return new LowTyre();
    }
}
