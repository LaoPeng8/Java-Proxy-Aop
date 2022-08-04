package org.pjj.gof23.singleton;

/**
 * (线程安全, 调用效率高, 不能延时加载)
 *
 * 枚举本身就单例模式. JVM从根本上提供保障! 可以避免通过反射和反序列化的漏洞!
 * @author PengJiaJun
 * @Date 2022/08/04 16:38
 */
public enum Demo05 {

    INSTANCE;

    //可以添加自己需要的操作.
    public void singletonOperation(){};
}
