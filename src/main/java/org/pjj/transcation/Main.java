package org.pjj.transcation;

import org.pjj.aop.config.UserConfig;
import org.pjj.transcation.service.MemberService;
import org.pjj.transcation.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;

/**
 * @author PengJiaJun
 * @Date 2022/08/01 19:44
 */
public class Main {
    public static void main(String[] args) {
//        测试 编程式事务, 声明式事务, 注解式事务
//        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//        UserService userService = context.getBean(UserService.class);

//        userService.transfer("Tom", "jerry", 500);//转账 (没有事务)

//        userService.transfer3("Tom", "jerry", 500);//转账 (有事务)

//        userService.transfer4("Tom", "jerry", 500);//转账 (基于aspectj的声明式事务)

//        ===================================================================================

        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        MemberService memberService = context.getBean(MemberService.class);

//        Propagation.NEVER
//        memberService.A1("Tom", "jerry", 500);

//        Propagation.NOT_SUPPORTED
//        memberService.A2("Tom", "jerry", 500);

//        Propagation.SUPPORTED
//        memberService.A3("Tom", "jerry", 500);

//        Propagation.REQUIRES_NEW
//        memberService.A4("Tom", "jerry", 500);

//        Propagation.NESTED
//        memberService.A5("Tom", "jerry", 500);

//        Propagation.REQUIRED
//        memberService.A6("Tom", "jerry", 500);

//        Propagation.MANDATORY
        memberService.A7("Tom", "jerry", 500);

    }
}
