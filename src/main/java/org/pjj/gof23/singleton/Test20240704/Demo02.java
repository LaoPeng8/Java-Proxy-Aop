package org.pjj.gof23.singleton.Test20240704;

/**
 * 懒汉式
 * @author PengJiaJun
 * @Date 2024/07/04 14:53
 */
public class Demo02 {
    public static void main(String[] args) {
        Student2 student2 = Student2.getStudent2();
        student2.study();
    }
}

// 懒汉式单例类
class Student2 {

    private Student2(){}

    // 懒汉式之所以叫懒汉式就是因为懒, 可以延时加载, 即初始化该类的时候不会直接new出该对象, 而是等到真正使用获取该对象的方法后再new
    // 不能加 final 因为常量只能赋值一次, 加了之后 在getStudent2()方法中会编译报错
    private static Student2 student = null;

    // 该方法在判断 student等于null时即开始赋值, 最后返回student
    // 在多线程环境下线程是不安全的, 可能出现多次new Student2()的情况, 所以需要加上 synchronized
    // 调用效率肯定是没有饿汉式高的, 因为有synchronized的存在, 并且方法内部有判断
    public static synchronized Student2 getStudent2() {
        if(student == null) {
            student = new Student2();
        }

        return student;
    }

    // 单例类的功能
    public void study() {
        System.out.println("学习");
    }

}
