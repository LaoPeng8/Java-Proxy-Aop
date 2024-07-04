package org.pjj.gof23.singleton.Test20240704;

/**
 * 饿汉式
 * @author PengJiaJun
 * @Date 2024/07/04 14:40
 */
public class Demo01 {
    public static void main(String[] args) {
        Student student = Student.getStudent();
        student.study();
    }
}

// 饿汉式单例类
class Student {

    // 私有构造 不让外部 直接new
    private Student(){}

    // 饿汉式之所以叫饿汉式, 是因为在该类被加载时会直接new出该对象 (这个对象可能比较大, 加载耗时, 所以可能在最开始加载时就加载了)
    // private 禁止外部访问
    // 提供外部访问的方法getStudent() 必须是static静态的 否则外部访问不了, 而static方法不能访问实例变量, 所以该变量需要static关键字
    // final 可以让该变量 变为常量 只能被赋值一次, 但是我觉得加不加final是没太大影响的, 但是实际肯定是要按规矩来给加上的
    private static final Student student = new Student();


    public static Student getStudent() {
        return student;
    }

    // 单例类的功能
    public void study() {
        System.out.println("学习");
    }

}

