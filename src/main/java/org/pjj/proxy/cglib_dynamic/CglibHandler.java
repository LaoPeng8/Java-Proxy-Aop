package org.pjj.proxy.cglib_dynamic;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * 我的理解
 * class CglibHandler implements MethodInterceptor该类就相当于是一个代理类, 因为代理逻辑写在该类中 (但是实际不是代理类, 代理类是根据Enhancer的create方法生成的)
 * 该类就有点类似 JDK动态代理中的 public class SellHandler implements InvocationHandler 这个实现了 InvocationHandler接口的类
 *
 * JDK动态代理中是    实现InvocationHandler接口 中的 invoke方法 完成代理逻辑
 * CGlib动态代理中是  实现MethodInterceptor接口 中的 intercept方法 完成代理逻辑
 *
 * JDK动态代理是通过    Proxy.newProxyInstance 来生成代理对象的
 * CGlib动态代理是通过  enhancer.create(); 来生成代理对象的
 *
 * JDK动态代理必须要实现接口  因为 Proxy.newProxyInstance时需要传入 接口.class
 * (因为要实现该接口, 然后生成一个 代理对象, 这个代理对象就有了这个接口中的方法, 目标对象(被代理对象)也实现了这个接口, 相当于代理对象就有了被代理对象的所有方法,
 * 代理对象虽然有了被代理对象的所有方法, 但是实现都一样 就是 invoke方法中的实现, invoke中就会判断用户实际调用的是 目标对象(被代理对象)的什么方法, 然后调用目标方法,
 * 调用目标方法前, 会先执行 代理的逻辑, 即在调用真正的目标方法前后 可以有属于代理的逻辑)
 * (所以 JDK动态代理是基于实现接口)
 *
 * CGlib动态代理则不需要实现接口, 只需要传入一个父类(即被代理对象)
 * (JDK动态代理是通过实现接口, 来变相的拥有目标对象(被代理对象)的方法, CGlib则是通过继承父类(继承目标对象(被代理对象))来让代理对象变相的拥有 目标对象的方法,
 * 代理对象虽然有了被代理对象的所有方法, 但是实现都一样 就是 intercept方法中的实现, intercept中就会判断用户实际调用的是 目标对象(被代理对象)的什么方法, 然后调用目标方法,
 * 调用目标方法前, 会先执行 代理的逻辑, 即在调用真正的目标方法前后 可以有属于代理的逻辑)
 * (所以 CGlib动态代理是基于继承的)
 * (所以 CGlib动态代理不能代理被 final修饰的类, 因为final修饰的类不可被继承. 也不能代理final修饰的方法, 因为final修饰方法 该方法不能被子类覆盖(重写))
 *
 * 如果对象有接口实现,选择JDK代理
 * 如果没有接口实现选择CGILB代理
 *
 *
 * @author PengJiaJun
 * @Date 2022/07/24 12:35
 */
public class CglibHandler implements MethodInterceptor {

    private Object target;//目标对象, 通过构造器传入

    public CglibHandler(Object target) {
        this.target = target;
    }

    /**
     * 用来获取代理对象 (创建一个代理对象)
     */
    public Object getProxy() {
        //通过该 Enhancer 来生成代理对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());//设置父类(将目标类作为代理类的父类)
        enhancer.setCallback(this);//设置拦截器(回调对象为本身对象), 即执行目标方法前后, 增强的功能在本类, 即类似本来是代理类的逻辑

        return enhancer.create();//生成一个代理类对象并返回给调用者
    }

    /**
     * 该方法就是 代理类的逻辑, 即在该方法类 执行真正的目标方法, 且可以在执行真正的目标方法前后 加上代理的逻辑, 即前置通知 后置通知
     * @param o (不理解)
     * @param method 实体类所调用的都被代理的方法的引用, 即实体类调的方法的Class对象
     * @param objects 实体类所调用方法的参数列表
     * @param methodProxy 生成的代理类对方法的代理引用 (不理解)
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object res = null;
        if(method.getName().equals("test")) {
            System.out.println("方法执行前的代理逻辑");
            res = methodProxy.invoke(target, objects);//调用目标方法 (真正需要执行的方法)
            System.out.println("方法执行后的代理逻辑");
        }

        return res;
    }
}
