package org.pjj.gof23.factory03;

/**
 * 低端座椅
 * @author PengJiaJun
 * @Date 2022/08/05 17:10
 */
public class LowSeat implements Seat {
    @Override
    public void message() {
        System.out.println("不仅不会按摩, 还疙屁股");
    }
}
