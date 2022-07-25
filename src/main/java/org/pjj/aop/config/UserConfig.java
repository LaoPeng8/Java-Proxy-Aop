package org.pjj.aop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author PengJiaJun
 * @Date 2022/07/25 16:36
 */
@Configuration
@ComponentScan("org.pjj.aop")//扫描组件, 要不然@Component不起作用
@EnableAspectJAutoProxy//开启AspectJ语法, 要不然@Before等SpringAop注解不会生效, 注意SpringBoot该注解默认开启的
public class UserConfig {
}
