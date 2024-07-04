package org.pjj.gof23.singleton.Test20240704;

/**
 * 静态内部类式
 * @author PengJiaJun
 * @Date 2024/07/04 15:11
 */
public class Demo04 {
    public static void main(String[] args) {
        Student4 student4 = Student4.getStudent4();
        student4.study();
    }
}

// 静态内部类式
class Student4 {

    private Student4(){}

    // 利用类加载时天然的线程安全来 new出单例类
    // 属性全是private的, 避免外部通过 Student4.Student44.student4 这种方式得到单例类对象
    private static class Student44 {
        private static final Student4 student4 = new Student4();
    }

    public static Student4 getStudent4() {
        return Student44.student4;
    }

    public void study() {
        System.out.println("学习");
    }

}
