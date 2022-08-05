package org.pjj.gof23.factory03;

/**
 * 抽象工厂模式测试
 * @author PengJiaJun
 * @Date 2022/08/05 17:39
 */
public class Main {
    public static void main(String[] args) {
        LuxuryCarFactory luxuryCarFactory = new LuxuryCarFactory();
        Engine engine = luxuryCarFactory.createEngine();
        Seat seat = luxuryCarFactory.createSeat();
        Tyre tyre = luxuryCarFactory.createTyre();
        engine.run();
        engine.start();
        seat.message();
        tyre.revolve();
    }
}
