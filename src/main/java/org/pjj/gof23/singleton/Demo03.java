package org.pjj.gof23.singleton;

/**
 * 双重检查锁式(由于JVM底层内存模型原因, 偶尔出现问题, 不建议使用)
 * 了解即可
 *
 * @author PengJiaJun
 * @Date 2022/08/04 16:20
 */
public class Demo03 {

    private static Demo03 instance = null;

    private Demo03(){};

    public Demo03 getInstance() {
        if(instance == null) {
            Demo03 demo03;
            synchronized (Demo03.class) {
                demo03 = instance;
                if(demo03 == null) {
                    synchronized (Demo03.class) {
                        if(demo03 == null) {
                            demo03 = new Demo03();
                        }
                    }
                    instance = demo03;
                }
            }
        }
        return instance;
    }

}
