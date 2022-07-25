package org.pjj.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 测试 Spring Aop 打印日志
 * @author PengJiaJun
 * @Date 2022/07/25 15:49
 */
@Aspect
@Component
public class LogAspect {

    /**
     * 表示 UserController类中的所有方法, 方法参数任意(一个), 修饰符任意 (切点), 即切面会插入这些切点中
     * execution(* org.pjj.aop.controller.UserController.*(*))
     *
     * 上述看的不是很明显, 看这个完整的就比较好理解
     *
     * 这个就比较好理解了是指定了切点为  public void addUser(User user);方法
     * execution(public void org.pjj.service.impl.UserServiceImpl.addUser(org.pjj.entity.User))")
     *
     * //这个就是 任意修饰符, com.pjj.controller包下任意层级的Controller类中的任意方法, 参数任意
     * execution(* com.pjj.controller..*.*(..))
     */
    @Pointcut("execution(* org.pjj.aop.controller.UserController.*(*))")//表示切点
    public void log() {
    }

    @Before("log()")
    public void doBeforeLog(JoinPoint jp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        System.out.println("日志===== 时间: " + date +", 请求方法: " + jp.getSignature());

//        System.out.println("~~注解形式实现的-前置通知~~" +
//                "获取目标对象:"+ jp.getTarget()+
//                ",获取方法名"+ jp.getSignature()+
//                ",参数列表"+ Arrays.toString(jp.getArgs()));
    }

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
