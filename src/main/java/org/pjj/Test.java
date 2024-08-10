package org.pjj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author PengJiaJun
 * @Date 2024/08/06 17:01
 */
public class Test {
    int i = qotJ();
    int j = 0;
    public int qotJ() {
        j = 10;
        System.out.println(i);
        return j;
    }

    public static void main(String[] args) {
        String str = "aa.bb.cc";
        System.out.println(Arrays.toString(str.split("\\    .")));
    }
}

abstract class A {
    abstract void a1();

    void a2() {
    }
}

class B extends A {
    void a1() {
    }

    void a2() {
    }
}

class C extends B {
    void c1() {
    }
}
