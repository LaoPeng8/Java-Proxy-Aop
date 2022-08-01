package org.pjj.transcation;

import org.pjj.aop.config.UserConfig;
import org.pjj.transcation.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author PengJiaJun
 * @Date 2022/08/01 19:44
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        UserService userService = context.getBean(UserService.class);

//        userService.transfer("Tom", "jerry", 500);//转账 (没有事务)

//        userService.transfer3("Tom", "jerry", 500);//转账 (有事务)

        userService.transfer4("Tom", "jerry", 500);//转账 (基于aspectj的声明式事务)

    }
}
