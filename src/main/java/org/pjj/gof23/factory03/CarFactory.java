package org.pjj.gof23.factory03;

/**
 * @author PengJiaJun
 * @Date 2022/08/05 17:36
 */
public interface CarFactory {

    Engine createEngine();
    Seat createSeat();
    Tyre createTyre();

}
