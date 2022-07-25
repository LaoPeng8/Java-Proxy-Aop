package org.pjj.aop;

import net.sf.cglib.core.DebuggingClassWriter;
import org.pjj.aop.config.UserConfig;
import org.pjj.aop.controller.ProductController;
import org.pjj.aop.controller.UserController;
import org.pjj.aop.controller.UserControllerInterface;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author PengJiaJun
 * @Date 2022/07/25 15:47
 */
public class Main {
    public static void main(String[] args) {
        //开启cglib debug模式, 可以看到cglib动态生成类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"D:/eatMeal/JavaNB/IDEA/ideaProject/Java-Proxy-Aop/target/classes");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(UserConfig.class);
        UserControllerInterface userControllerInterface = (UserControllerInterface) context.getBean("userController");

        /**
         * 结果:
         * 日志===== 时间: 2022-07-25 16:45:14, 请求方法: String org.pjj.aop.controller.UserController.queryUserById(Long)
         * select * from user where id = 1
         *
         * 这就是插入切面后的执行结果
         */
        String name = userControllerInterface.queryUserById(001L);//执行方法

        /**
         * 给爷看傻了, 老师这里不是同一个类型, 而我确实同一个类型:
         * 我的思考是, 不管是JDK动态代理 还是 CGlib动态代理, 最后结果应该都是同一个类型,
         * 因为 JDK是需要接口, 代理对象实现接口, 那么代理对象与接口肯定是同一个类型嘛
         * CGlib需要继承, 代理对象继承目标对象, 那么代理对象与UserController肯定是同一个类型嘛 (子类 instanceof 父类, true)
         *
         * 后来发现老师是实现接口了的, 我也实现了接口 发现确实不是同一个类型了.
         * 我的发现, 发现之前没有实现接口时编译后的文件中有 CGlib的代理对象, 说明目标类没有实现接口时 是使用CGlib动态代理
         * 后来我实现接口后发现遍历后的文件中没有了 CGlib的代理对象, 说明目标类实现接口时 是使用JDK动态代理
         *
         * 为什么 实现接口后 发现确实不是同一个类型了?
         * 因为 JDK代理生成了一个 实现UserControllerInterface的代理对象
         * 代理对象与 UserController之间没有直接关系 虽然都实现了UserControllerInterface接口
         * 所以 代理对象也就是 getBean()出来的对象 不能强转为 UserController类型 只能强转为 UserControllerInterface
         * userControllerInterface instanceof UserControllerInterface  (true)
         * userControllerInterface instanceof UserController (false)
         */
        if(userControllerInterface instanceof UserController) {
            System.out.println("插入切面后 是同一个类型");
        }else{
            System.out.println("插入切面后 不是同一个类型");
        }

        System.out.println("\n=======================================================\n");

        ProductController product = context.getBean(ProductController.class);
        product.sellProduct();//该方法原本不会打印金额, 通过SpringAop打印金额
        product.sellProduct2();//该方法原本不会打印金额, 通过SpringAop打印金额

    }
}
