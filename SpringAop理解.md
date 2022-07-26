# SpringAop理解

> 作者: LaoPeng
>
> 2022/7/25 13:31 始

让关注点代码与业务代码分离,可以动态地添加和删除在切面上的逻辑而不影响原来的执行代码。

SpringAop 与 Aspectj 没什么关系, SpringAop底层也不是使用的Aspectj 只是借用了 Aspectj的语法(因为Aspectj的语法很简单),
说是借用了Aspectj的语法, 实际还借用了一些注解如 @Aspect, @Before 所以这些都是来自org.aspectj.lang.annotation包下的,
所以使用SpringAop除了引入 spring-aop.jar 还需要pom依赖 => org.aspectj.aspectjweaver

## 首先我们编写一个了普通的前置通知的例子
```java
public interface UserControllerInterface {
    public String queryUserById(Long id);
}

@Component
public class UserController implements UserControllerInterface {
    //模拟请求, 在执行Controller方法前 先使用SpringAop前置通知 打印 日志
    public String queryUserById(Long id) {
        System.out.println("select * from user where id = " + id);
        return "张三";
    }
}

@Aspect
@Component
public class LogAspect {
    @Pointcut("execution(* org.pjj.aop.controller.UserController.*(*))")//表示切点
    public void log() {
    }

    @Before("log()")
    public void doBeforeLog(JoinPoint jp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        System.out.println("日志===== 时间: " + date +", 请求方法: " + jp.getSignature());
    }
}

@Configuration
@ComponentScan("org.pjj.aop")//扫描组件, 要不然@Component不起作用
@EnableAspectJAutoProxy//开启AspectJ语法, 要不然@Before等SpringAop注解不会生效, 注意SpringBoot该注解默认开启的
public class UserConfig {
}

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

    }
}
```
结果上面的测试, 我发现,之前没有实现接口时编译后的文件中有 CGlib的代理对象, 说明**目标类没有实现接口时 是使用CGlib动态代理**, 
后来我实现接口后发现遍历后的文件中没有了 CGlib的代理对象, **说明目标类实现接口时 是使用JDK动态代理**

而且我发现 不管使用JDK还是CGlib时 都会将我的 配置类UserConfig生成出一份代理对象, 我也不知道为什么  
public class UserConfig$$EnhancerBySpringCGLIB$$25b78efa extends UserConfig implements EnhancedConfiguration {


## cglib代理与jdk代理效率比较
1、CGLib所创建的动态代理对象在实际运行时候的性能要比JDK动态代理高不少，有研究表明，大概要高10倍;
2、但是CGLib在创建对象的时候所花费的时间却比JDK动态代理要多很多，有研究表明，大概有8倍的差距;
3、因此，对于singleton的代理对象或者具有实例池的代理，因为无需频繁的创建代理对象，所以比较适合采用CGLib动态代理，反则比较适用JDK动态代理。

结果是不是如上边1、2、3条描述的那样哪?下边我们做一些小实验分析一下!

最终的测试结果大致是这样的，在**1.6和1.7的时候，JDK动态代理的速度要比CGLib动态代理的速度要慢**，但是并没有教科书上的10倍距，
**在JDK1.8的时候，JDK动态代理的速度已经比CGLib动态代理的速度快很多**了，希望小伙伴在遇到这个问题的时候能够有的放矢!

## execution解释
```text
表示 UserController类中的所有方法, 方法参数任意(一个), 修饰符任意 (切点), 即切面会插入这些切点中
execution(* org.pjj.aop.controller.UserController.*(*))

上述看的不是很明显, 看这个完整的就比较好理解

这个就比较好理解了是指定了切点为  public void addUser(User user);方法
execution(public void org.pjj.service.impl.UserServiceImpl.addUser(org.pjj.entity.User))")

这个就是 任意修饰符, com.pjj.controller包下任意层级的Controller类中的任意方法, 参数任意
execution(* com.pjj.controller..*.*(..))
```

## 自定义注解
除了使用 execution表达式外还有  
@within和@target是在配置切点的时候使用到的两个修饰符，都是基于注解来配置切点。

比如当前有注解@A

"@within(com.annotation.other.A)"该配置就是：如果某个类上标注了注解@A，那么该类中的所有方法就会被匹配为切点，并且该类的子类中没有重写的父类方法也会被匹配为切点  
"@target(com.annotation.other.A)"该配置就是：如果某个类上标注了注解@A，那么该类中的所有方法就会被匹配为切点。  
需要注意的 @within(org.pjj.A1) 与 @target(org.pjj.A1) 只能精确到类, 即被该表达式的注解(A1)标注的类 中所有的方法都是切点

@annotation()
```java
public @interface M {
}

@Aspect
@Component
public class LogAspect {
    //...
    
    /**
     * 这个切点就是 所有被 org.pjj.aop.annotation包中的 @M 注解标识的方法 都是切点
     */
    @Pointcut("@annotation(org.pjj.aop.annotation.M)")
    public void money() {
    }

    @AfterReturning(pointcut = "money()", returning = "returningValue")//返回后通知： 执行方法结束前执行(异常不执行)
    public void doAfterMoney(double returningValue) {
        System.out.println("花费金额: " + returningValue);
    }

}

@Component
public class ProductController {

    @M //被@M注释标注了, 又由@Pointcut("@annotation(org.pjj.aop.annotation.M)"), 所以该方法会由动态代理代理, 在执行该方法前后 执行代理逻辑
    public double sellProduct() {
        System.out.println("购买了一件勇闯天涯(12瓶)");
        return 28.8;
    }
    @M
    public double sellProduct2() {
        System.out.println("购买了一个iPhone13");
        return 5888;
    }
}

ProductController product = context.getBean(ProductController.class);
product.sellProduct();//该方法原本不会打印金额, 通过SpringAop打印金额
product.sellProduct2();//该方法原本不会打印金额, 通过SpringAop打印金额
 
结果:
购买了一件勇闯天涯(12瓶)
花费金额: 28.8
购买了一个iPhone13
花费金额: 5888.0
```