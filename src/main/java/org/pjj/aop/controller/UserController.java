package org.pjj.aop.controller;

import org.springframework.stereotype.Component;

/**
 *
 * 测试 SpringAop前置通知, 使用面向切面的方式, 在不修改主业务逻辑的同时, 插入切面(辅助逻辑 如日志)
 *
 * @author PengJiaJun
 * @Date 2022/07/25 15:46
 */
@Component
public class UserController implements UserControllerInterface {

    /**
     * 模拟请求, 在执行Controller方法前 先使用SpringAop前置通知 打印 日志
     * @param id
     * @return
     */
    @Override
    public String queryUserById(Long id) {
        System.out.println("select * from user where id = " + id);
        return "张三";
    }
}
