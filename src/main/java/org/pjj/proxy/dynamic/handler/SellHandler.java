package org.pjj.proxy.dynamic.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author PengJiaJun
 * @Date 2022/07/23 01:29
 */
public class SellHandler implements InvocationHandler {//必须实现 InvocationHandler , 在invoke()方法中完成 代理类的功能, 即在目标方法执行前后 如打印日志等(执行代理功能)

    private Object target = null;//目标对象

    public SellHandler(Object target) {//通过构造器将 目标对象传入, 所以是动态的, 传入什么目标对象, 最后就会执行什么.
        super();
        this.target = target;
    }

    /**
     *
     * @param proxy 代理对象
     * @param method 目标对象的目标方法
     * @param args 目标方法的方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("sell")) {
            float price = (float) method.invoke(target, args);//调用目标方法
            System.out.println("出厂价: " + price);

            //在目标方法之后执行 (增强功能)
            float newPrice = ((int) args[0] * 10 + price);//商家的价格 (每个U盘赚10块)
            System.out.println("商家价: " + newPrice);
            System.out.println("商家提供物流服务, 退换货服务, 客服服务");

            return newPrice;
        }
        if(method.getName().equals("sayHello")) {
            method.invoke(target, null);//调用目标方法
            System.out.println("代理对象也对你说hello");//增强功能
        }
        return null;
    }
}
