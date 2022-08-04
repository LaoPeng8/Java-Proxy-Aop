package org.pjj.gof23.singleton;

/**
 * 饿汉式(线程安全, 调用效率好(调用时不需要new), 但是不能延时加载)
 *
 * 初始化类时立即加载对象, 不能延时加载, 如果该对象创建比较耗时, 后期又不使用的话, 就比较浪费资源(内存?)
 *
 * @author PengJiaJun
 * @Date 2022/08/04 12:12
 */
public class Demo01 {

    //private 限定不能其他人访问该属性, 继承不到该属性
    //静态方法只能使用静态属性, 所以该属性需要是static的
    //final 表示常量, 即该属性初始化后不能被修改.
    private static final Demo01 demo01 = new Demo01();//加载类时天然的线程安全, 保证只会new出一个对象. 类初始化时,立即加载该对象

    private Demo01(){} //私有构造, 不让其他人new

    /**
     * 为什么要是 static, 如果不是static那么这个方法就不能通过 类.方法名调用, 那么就还需要new出一个对象, 我如果能new出一个对象还是单例模式吗!!!
     * 方法没有使用 synchronized 调用效率高. (不用使用synchronize, 无论怎么调用返回的都是 demo01)
     */
    public static Demo01 getInstance() {
        return demo01;
    }
}
