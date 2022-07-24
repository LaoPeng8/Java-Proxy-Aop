package org.pjj.proxy.cglib_dynamic;

/**
 * @author PengJiaJun
 * @Date 2022/07/24 12:27
 */
public class UserService {

    public void test() {
        System.out.println("目标方法执行test (被代理的对象)");
    }

    public void test2() {
        System.out.println("目标方法执行(被代理的对象) test2  test2  test2  test2");
    }

}
