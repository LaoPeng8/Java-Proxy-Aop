package org.pjj.gof23.singleton.Test20240704;

/**
 * 双重检查锁
 * @author PengJiaJun
 * @Date 2024/07/04 15:02
 */
public class Demo03 {
    public static void main(String[] args) {
        Student3 student3 = Student3.getStudent3();
        student3.study();
    }
}

// 双重检查锁 单例类
class Student3 {

    // 首先私有构造
    private Student3(){}

    // 双重检查锁 也是延时加载的, 所以不能加 final
    // 此处的 volatile 是防止 指令重排
    private static volatile Student3 student3 = null;

    // 提供给外部获取单例类对象的方法
    // 最外层的 if 可以防止很多线程阻塞在 synchronized处 (如果不要也能用, 效率就低了很多)
    // synchronized只能锁Student3类, 所不了this, 锁this锁的是调用这个对象的方法, 这是静态方法不需要对象来调用
    public static Student3 getStudent3() {
        if(student3 == null) {
            synchronized (Student3.class) {
                if(student3 == null) {
                    student3 = new Student3();
                }
            }
        }

        return student3;
    }

    // 单例类的功能
    public void study() {
        System.out.println("学习");
    }

}