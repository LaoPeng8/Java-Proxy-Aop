package org.pjj.gof23.singleton;

/**
 * 静态内部类式(线程安全, 调用效率高, 可以延时加载)
 *
 * 外部类没有static属性, 则不会像饿汉式那样立即加载单例对象;
 * 只有真正调用getInstance()时才会加载静态内部类。加载类时线程是安全的。
 * static final 保证了内存中只有这样一个实例, 而且只能被赋值一次
 *
 * 兼并了并发高效调用 和 延时加载的优势
 *
 * @author PengJiaJun
 * @Date 2022/08/04 16:26
 */
public class Demo04 {

    private Demo04(){};

    private static class SingletonClassInstance {
        private static final Demo04 demo04 = new Demo04();
    }

    public static Demo04 getInstance() {
        return SingletonClassInstance.demo04;
    }


}
