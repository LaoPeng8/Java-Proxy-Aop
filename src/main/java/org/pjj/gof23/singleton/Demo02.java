package org.pjj.gof23.singleton;

/**
 * 懒汉式(线程安全, 调用效率不高(调用时需要new), 但是可以延时加载)
 *
 * 初始化类时不会加载对象, 可以能延时加载(真正使用时加载). 资源利用率高了, 但是每次调用getDemo02()方法都需要synchronized, 并发效率低了.
 *
 * @author PengJiaJun
 * @Date 2022/08/04 12:38
 */
public class Demo02 {

    private static Demo02 demo02 = null;//类加载时不会立即初始化 单例对象

    private Demo02(){};//私有构造, 不让其他人new

    /**
     * 需要 synchronized , 因为如果不加锁, 那么一个线程判断 demo02 == null时进入if,正在new还没有赋值,
     * 另一个线程判断 demo02 == null时进入if,开始new, 这样就导致单例失败 (单例对象不只一个对象), 完犊子了
     */
    public static synchronized Demo02 getInstance() {
        //延时加载, 只有在调用方法时才会初始化 单例对象
        if(demo02 == null) {
            demo02 = new Demo02();
        }

        return demo02;
    }

}
