package org.pjj.proxy.cglib_dynamic;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author PengJiaJun
 * @Date 2022/07/24 12:29
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"D:/eatMeal/JavaNB/IDEA/ideaProject/Java-Proxy-Aop/target/classes");
        final UserService target = new UserService();

        CglibHandler cglibHandler = new CglibHandler(target);//传入目标对象

        UserService userInterface = (UserService) cglibHandler.getProxy();//得到代理对象

        userInterface.test2();//执行的test方法就有了, 代理的前后逻辑增强
    }
}
