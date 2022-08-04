package org.pjj.gof23.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 饿汉式(线程安全, 调用效率好(调用时不需要new), 但是不能延时加载)
 *
 * 以饿汉式为例子防止 反射 和 反序列化漏洞
 *
 * @author PengJiaJun
 * @Date 2022/08/04 16:49
 */
public class Demo06 implements Serializable {

    private static final Demo06 instance = new Demo06();//加载类时天然的线程安全, 保证只会new出一个对象. 类初始化时,立即加载该对象

    /**
     * 当第一次new时, instance肯定等于null, 就让new, 然后就给instance赋值了, 单例对象出现了
     * 当其他用户通过反射调用该构造器生成对象时, 由于instance!=null 就会抛出异常
     *
     * 但是这种方式只能防止饿汉式的 反射漏洞, 防止不了 懒汉式的 反射漏洞
     * 因为之所以可以防止饿汉式是因为 饿汉式第一次new 肯定是在 本类被初始化时 在本类中被new的, 也就是赋值给了 instance,
     * 所以第二次new的时候 instance已经!=null 了所以会抛出异常
     *
     * 但是 懒汉式是在调用 getInstance()方法时判断 instance == null 才会 new出一个对象赋值给 instance,
     * 所以只要instance == null(即不要给 instance赋值, 即不要调用getInstance()方法), 就可以一直利用反射new
     *
     *
     */
    private Demo06(){
        if(instance != null) {
            throw new RuntimeException("单例对象不可以被new (别他妈的用反射调我了)");
        }
    } //私有构造, 不让其他人new

    public static Demo06 getInstance() {
        return instance;
    }

    //通过该方法防止 反序列化漏洞
    //反序列化时, 如果定义了readResolve()则直接返回此方法指定的对象, 而不需要单独再创建新对象
    public Object readResolve() throws ObjectStreamException {
        return instance;
    }
}
