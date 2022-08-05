package org.pjj.gof23.factory03;

/**
 * 高端发动机
 * @author PengJiaJun
 * @Date 2022/08/05 17:07
 */
public class LuxuryEngine implements Engine {
    @Override
    public void run() {
        System.out.println("转的快!");
    }

    @Override
    public void start() {
        System.out.println("启动快! 可以自动启停!");
    }
}
