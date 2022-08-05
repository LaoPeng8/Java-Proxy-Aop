package org.pjj.gof23.factory03;

/**
 * 低端轮胎
 * @author PengJiaJun
 * @Date 2022/08/05 17:13
 */
public class LowTyre implements Tyre {
    @Override
    public void revolve() {
        System.out.println("磨损快!");
    }
}
