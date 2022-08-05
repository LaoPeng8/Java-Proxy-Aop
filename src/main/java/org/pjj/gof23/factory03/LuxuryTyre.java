package org.pjj.gof23.factory03;

/**
 * 高端轮胎
 * @author PengJiaJun
 * @Date 2022/08/05 17:12
 */
public class LuxuryTyre implements Tyre {
    @Override
    public void revolve() {
        System.out.println("旋转不磨损!");
    }
}
