package org.pjj.proxy.dynamic;

import org.pjj.proxy.dynamic.factory.UsbKingFactory;
import org.pjj.proxy.dynamic.handler.SellHandler;
import org.pjj.proxy.dynamic.service.UsbSell;

import java.lang.reflect.Proxy;

/**
 * @author PengJiaJun
 * @Date 2022/07/23 13:13
 */
public class ShopMain {
    public static void main(String[] args) {
        UsbKingFactory factory = new UsbKingFactory();
        SellHandler sellHandler = new SellHandler(factory);//传入目标对象

        /**
         * 获取代理对象
         *
         * 参数一 类加载器
         * 参数二 目标对象的接口, 换句话说 动态代理动态代理, 总要有目标对象吧, 要不然给谁代理, 所以说根据目标对象的接口, 生成代理对象, 代理对象也实现了该接口, 即 代理对象 与 目标对象 实现的功能一致(只不过代理对象在目标对象的基础上 加强了功能)
         * 参数三 代理对象需要做的事, 即 动态代理只是生成代理对象, 代理对象实际要干什么 还是由我们自己指定 即 代理对象的具体实现还是由我们实现
         */
//        UsbSell usbSell = (UsbSell) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{UsbSell.class}, sellHandler);
        UsbSell usbSell = (UsbSell) Proxy.newProxyInstance(factory.getClass().getClassLoader(), factory.getClass().getInterfaces(), sellHandler);

        float sell = usbSell.sell(10);
        System.out.println("\n----------------\n");
        usbSell.sayHello();

    }
}
