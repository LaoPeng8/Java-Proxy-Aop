# CGlib动态代理使用与原理分析

> 作者: LaoPeng
>
> 2022/7/24 12:18 始

## CGlib代理 与 JDK代理 的区别
```java
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


public class UserService {

    public void test() {
        System.out.println("目标方法执行test (被代理的对象)");
    }

}


public class Main {
    public static void main(String[] args) {
        final UserService target = new UserService();

        CglibHandler cglibHandler = new CglibHandler(target);//传入目标对象

        UserService userInterface = (UserService) cglibHandler.getProxy();//得到代理对象

        userInterface.test();//执行的test方法就有了, 代理的前后逻辑增强
    }
}
```

## CGlib源码分析
(源码感觉看的不是很明白, 我觉得到不是因为难, 这方面的视频比较少, 老师也只是简单介绍一下, 过一过源码, 不细致, 所以很难理解)
最终CGlib所产生的代理类长什么样子, 我们都没见过, 有什么方式可以看到这个最终的代理类长什么样子呢, 其实是可以的

JVM运行参数加 `-Dcglib.debugLocation=D:\eatMeal\JavaNB\IDEA\ideaProject\Java-Proxy-Aop\target\classes` 就可以了  
-Dcglib.debugLocation表示开启这个 debug模式, 后面的地址跟的是cglib产生的代理class放在哪里  
但是不知道为什么我不可以, 感觉单词也没拼错, 一模一样 就是不行

网上找的第二种方式 (这种方式成功了)  
在main方法中的第一行添加如下代码即可: `System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"D:/eatMeal/JavaNB/IDEA/ideaProject/Java-Proxy-Aop/target/classes");`

运行后可以发现生成了三个对象  
public class UserService$$EnhancerByCGLIB$$227c544b extends UserService implements Factory  
public class UserService$$EnhancerByCGLIB$$227c544b$$FastClassByCGLIB$$4983fca extends FastClass  
public class UserService$$FastClassByCGLIB$$48eb05b5 extends FastClass  

```java
/**
 * 先看一下这个类, 很显然这个类继承了目标类(被代理对象)UserService, 这个类就是代理类
 */
public class UserService$$EnhancerByCGLIB$$227c544b extends UserService implements Factory {
    private boolean CGLIB$BOUND;
    public static Object CGLIB$FACTORY_DATA;
    private static final ThreadLocal CGLIB$THREAD_CALLBACKS;
    private static final Callback[] CGLIB$STATIC_CALLBACKS;
    private MethodInterceptor CGLIB$CALLBACK_0;//拦截器, 即我们编写的 public class CglibHandler implements MethodInterceptor, 通过它来调用该类中我们编写的intercept()实现代理逻辑
    private static Object CGLIB$CALLBACK_FILTER;

    /**
     * 可以看到这些属性 都是 目标类(被代理对象)UserService 中有的属性或方法 (包含本身得 与 继承父类Object得)
     * (目标类中得方法 在此处都是两个属性) (目标对象一个方法, 对应本类就会有两个方法)
     * 
     * 可以发现这些属性或方法 都有两份, 比如 test方法, 这里有 Method的, 与 MethodProxy的 两种
     * Method对象就是对应目标类中的方法的Method对象
     */
    private static final Method CGLIB$test$0$Method;// Method test方法
    private static final MethodProxy CGLIB$test$0$Proxy;// MethodProxy test方法
    private static final Object[] CGLIB$emptyArgs;
    private static final Method CGLIB$test2$1$Method;// Method test2方法
    private static final MethodProxy CGLIB$test2$1$Proxy;// MethodProxy test2方法
    private static final Method CGLIB$equals$2$Method;// Method equals方法
    private static final MethodProxy CGLIB$equals$2$Proxy;// MethodProxy equals方法
    private static final Method CGLIB$toString$3$Method;
    private static final MethodProxy CGLIB$toString$3$Proxy;
    private static final Method CGLIB$hashCode$4$Method;
    private static final MethodProxy CGLIB$hashCode$4$Proxy;
    private static final Method CGLIB$clone$5$Method;
    private static final MethodProxy CGLIB$clone$5$Proxy;

    static void CGLIB$STATICHOOK1() {
        CGLIB$THREAD_CALLBACKS = new ThreadLocal();
        CGLIB$emptyArgs = new Object[0];
        Class var0 = Class.forName("org.pjj.proxy.cglib_dynamic.UserService$$EnhancerByCGLIB$$227c544b");
        Class var1;
        Method[] var10000 = ReflectUtils.findMethods(new String[]{"equals", "(Ljava/lang/Object;)Z", "toString", "()Ljava/lang/String;", "hashCode", "()I", "clone", "()Ljava/lang/Object;"}, (var1 = Class.forName("java.lang.Object")).getDeclaredMethods());
        CGLIB$equals$2$Method = var10000[0];
        CGLIB$equals$2$Proxy = MethodProxy.create(var1, var0, "(Ljava/lang/Object;)Z", "equals", "CGLIB$equals$2");
        CGLIB$toString$3$Method = var10000[1];
        CGLIB$toString$3$Proxy = MethodProxy.create(var1, var0, "()Ljava/lang/String;", "toString", "CGLIB$toString$3");
        CGLIB$hashCode$4$Method = var10000[2];
        CGLIB$hashCode$4$Proxy = MethodProxy.create(var1, var0, "()I", "hashCode", "CGLIB$hashCode$4");
        CGLIB$clone$5$Method = var10000[3];
        CGLIB$clone$5$Proxy = MethodProxy.create(var1, var0, "()Ljava/lang/Object;", "clone", "CGLIB$clone$5");
        var10000 = ReflectUtils.findMethods(new String[]{"test", "()V", "test2", "()V"}, (var1 = Class.forName("org.pjj.proxy.cglib_dynamic.UserService")).getDeclaredMethods());
        CGLIB$test$0$Method = var10000[0];
        CGLIB$test$0$Proxy = MethodProxy.create(var1, var0, "()V", "test", "CGLIB$test$0");
        CGLIB$test2$1$Method = var10000[1];
        CGLIB$test2$1$Proxy = MethodProxy.create(var1, var0, "()V", "test2", "CGLIB$test2$1");
    }


    /**
     * 可以发现这些方法都有两份, 比如test方法就对应了两份与test有关的方法
     * 一份是调用父类的test().   即 super.test();
     * 一份是调用 intercept().  即我们自己实现的 MethodInterceptor接口中的intercept(), 即代理类的逻辑(代理类所有方法实现应该都是调用intercept(), 然后我们在intercept()中编写代理类的逻辑)
     */

    final void CGLIB$test$0() {
        super.test();//调用父类的test方法, 即调用目标对象本身test()方法
    }

    /**
     * 可以发现 test() 方法的实现是 获取MethodInterceptor然后对象不存在则调用CGLIB$BIND_CALLBACKS(this);创建然后赋值
     * 根据MethodInterceptor调用intercept()方法, 也就是我们自己编写的intercept()方法, 该方法中包含着 代理对象的逻辑
     * 
     * 使用代理对象调用test()方法实际就调用该方法
     * 本方法一进入就会先拿到 MethodInterceptor对象 俗称 方法拦截器
     * 
     */
    public final void test() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;//该属性第一次肯定是为null的, 
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);//通过该方法获取到 方法拦截器
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            //this表示当前代理对象, CGLIB$test$0$Method表示调用的方法是哪个, CGLIB$emptyArgs表示方法的参数, CGLIB$test$0$Proxy表示
            var10000.intercept(this, CGLIB$test$0$Method, CGLIB$emptyArgs, CGLIB$test$0$Proxy);
        } else {
            super.test();
        }
    }

    //通过该方法获取到 方法拦截器          Object var0 就是this, 也就是代理类对象
    private static final void CGLIB$BIND_CALLBACKS(Object var0) {
        UserService$$EnhancerByCGLIB$$227c544b var1 = (UserService$$EnhancerByCGLIB$$227c544b)var0;
        if (!var1.CGLIB$BOUND) {//该属性默认就是false, !false 就是true
            var1.CGLIB$BOUND = true;//置为true, 下次调用test()方法时就不会再次从 ThreadLocal对象.get()来获取方法拦截器了

            //ThreadLocal对象.get()来获取 方法拦截器
            //那么ThreadLocal中的 方法拦截器是哪来的呢, 那么实际是我们在创建代理对象时处理的. 即 enhancer.create();这一段逻辑中处理的
            Object var10000 = CGLIB$THREAD_CALLBACKS.get();
            if (var10000 == null) {
                var10000 = CGLIB$STATIC_CALLBACKS;
                if (var10000 == null) {
                    return;
                }
            }

            //然后赋值给 方法拦截器
            var1.CGLIB$CALLBACK_0 = (MethodInterceptor)((Callback[])var10000)[0];
        }

    }

    final void CGLIB$test2$1() {
        super.test2();
    }

    public final void test2() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$test2$1$Method, CGLIB$emptyArgs, CGLIB$test2$1$Proxy);
        } else {
            super.test2();
        }
    }
    
    //equals...
    
    //toString...

    //hashCode...

    //clone...

    //......
}
```

enhancer.create();
```java
public class Enhancer extends AbstractClassGenerator {
    public Object create() {
        classOnly = false;
        argumentTypes = null;
        return createHelper();
    }

    private Object createHelper() {
        preValidate();
        //生成了一个EnhancerKey代理对象, 该代理对象里面保存了用户所设置的代理信息如 父类.class, 方法拦截器
        Object key = KEY_FACTORY.newInstance((superclass != null) ? superclass.getName() : null,
                ReflectUtils.getNames(interfaces),
                filter == ALL_ZERO ? null : new WeakCacheKey<CallbackFilter>(filter),
                callbackTypes,
                useFactory,
                interceptDuringConstruction,
                serialVersionUID);
        this.currentKey = key;
        //去创建代理类放入map缓存, key就是用户设置的代理信息如 父类.class, 方法拦截器,  value 就是代理类本身
        //因为可能用户在调用时 enhancer.create(), enhancer.create()... 生成好几个代理对象, 那么不可能每次都创建代理类,
        //那么当用户设置的代理信息一致时, 即key一致时, 是不会重复生成代理类的
        //注意: 这里是代理类, 不是代理对象. 代理类 好似模板, 可以new好多代理对象. 所有模板是不用重复产生的.
        Object result = super.create(key);
        return result;
    }
}

abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    
    //产生代理类, 也会根据代理类产生代理对象
    protected Object create(Object key) {
        try {
            ClassLoader loader = getClassLoader();
            Map<ClassLoader, ClassLoaderData> cache = CACHE;
            ClassLoaderData data = cache.get(loader);
            if (data == null) {
                synchronized (AbstractClassGenerator.class) {
                    cache = CACHE;
                    data = cache.get(loader);
                    if (data == null) {
                        Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<ClassLoader, ClassLoaderData>(cache);
                        // 构造方法中会生成代理类的lambda表达式
                        data = new ClassLoaderData(loader);
                        newCache.put(loader, data);
                        CACHE = newCache;
                    }
                }
            }
            this.key = key;
            
            // 利用ClassLoaderData拿到代理类, ClassLoaderData中有一个generatedClasses用来缓存生成好的代理类
            // this就是Enhancer
            Object obj = data.get(this, getUseCache());//该方法中获取代理类, getUseCache()是用户输入的, false表示不缓存, 默认缓存
            if (obj instanceof Class) {
                // 调用代理类的构造方法生成一个代理对象
                return firstInstance((Class) obj);
            }
            return nextInstance(obj);
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

    public Object get(AbstractClassGenerator gen, boolean useCache) {
        if (!useCache) {//不缓存 (不缓存则每次 enhancer.create();都需要重新生成代理类)
            return gen.generate(ClassLoaderData.this);
        } else {//缓存
            Object cachedValue = generatedClasses.get(gen);
            return gen.unwrapCachedValue(cachedValue);
        }
    }
    
}

public class Enhancer extends AbstractClassGenerator {
    protected Object firstInstance(Class type) throws Exception {
        if (classOnly) {
            return type;
        } else {
            return createUsingReflection(type);
        }
    }

    private Object createUsingReflection(Class type) {
        //设置 callbacks 到代理类中
        setThreadCallbacks(type, callbacks);
        try{

            if (argumentTypes != null) {

                return ReflectUtils.newInstance(type, argumentTypes, arguments);

            } else {

                return ReflectUtils.newInstance(type);

            }
        }finally{
            // clear thread callbacks to allow them to be gc'd
            setThreadCallbacks(type, null);
        }
    }

    private static void setThreadCallbacks(Class type, Callback[] callbacks) {
        setCallbacksHelper(type, callbacks, SET_THREAD_CALLBACKS_NAME);
    }
}
```