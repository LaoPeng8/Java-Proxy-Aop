package org.pjj.gof23.factory03;

/**
 * 高端座椅
 * @author PengJiaJun
 * @Date 2022/08/05 17:10
 */
public class LuxurySeat implements Seat {
    @Override
    public void message() {
        System.out.println("可以按摩!");
    }
}
