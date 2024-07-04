package org.pjj.gof23.singleton.Test20240704;

/**
 * 枚举单例
 * @author PengJiaJun
 * @Date 2024/07/04 15:21
 */
public class Demo05 {
    public static void main(String[] args) {
        Student5.student5.study();
    }
}

// 枚举本身就是单例
enum Student5 {

    student5;

    public void study() {
        System.out.println("学习");
    }

}

