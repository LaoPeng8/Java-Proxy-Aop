package org.pjj.gof23.factory03;

/**
 * 低端发动机
 * @author PengJiaJun
 * @Date 2022/08/05 17:08
 */
public class LowEngine implements Engine {

    @Override
    public void run() {
        System.out.println("转的慢!");
    }

    @Override
    public void start() {
        System.out.println("启动慢!");
    }
}
