package org.pjj.gof23.singleton;

/**
 * 双重检查锁式(由于JVM底层内存模型原因, 偶尔出现问题, 不建议使用)
 * 了解即可
 *
 * @author PengJiaJun
 * @Date 2022/08/04 16:20
 */
public class Demo03 {

    private static volatile Demo03 instance = null; //加入 volatile 防止指令重排, 导致单例出现问题

    private Demo03(){};

    public static Demo03 getInstance(){

        if(instance == null){//该if是为了, 多线程时不用每个线程都进来等待锁(如果 instance已经初始化了, 则之后得线程就不用每次都等待锁了)
            synchronized (Demo03.class){ // 加锁目的, 防止多线程同时进入造成对象多次实例化, 保证同时只有一个线程能进入
                if(instance == null){
                    instance = new Demo03() ;//不是一个原子性操作
                }
            }
        }
        return instance ;
    }

}
