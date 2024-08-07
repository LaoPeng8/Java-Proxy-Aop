package org.pjj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author PengJiaJun
 * @Date 2024/08/06 17:01
 */
public class Test {

    public static void hello() {
        System.out.println("hello");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.hello();

        Animal animal = new Animal() {
            @Override
            public void say() {

            }
        };
    }
}

abstract class Animal {
    public abstract void say();
}
