package org.pjj.gof23.factory03;

/**
 * 高端车工厂
 * @author PengJiaJun
 * @Date 2022/08/05 17:37
 */
public class LuxuryCarFactory implements CarFactory {
    @Override
    public Engine createEngine() {
        return new LuxuryEngine();
    }

    @Override
    public Seat createSeat() {
        return new LuxurySeat();
    }

    @Override
    public Tyre createTyre() {
        return new LuxuryTyre();
    }
}
