package org.pjj.transcation.service;

import org.pjj.transcation.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author PengJiaJun
 * @Date 2022/08/01 19:23
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionTemplate transactionTemplate;//编程式事务管理

    /**
     * 没有事务的转账业务
     * @param fromName
     * @param toName
     * @param money
     */
    public void transfer(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱
        if(true) {
            throw new RuntimeException("出错了!!!");
        }
        userDao.in(toName,money);//转入钱
    }

    /**
     * 开启了事务的转账业务 (声明式事务) (@Transactional)
     * @param fromName
     * @param toName
     * @param money
     */
    @Transactional
    public void transfer2(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱
        if(true) {
            throw new RuntimeException("出错了!!!");
        }
        userDao.in(toName,money);//转入钱
    }

    /**
     * 开启了事务的转账业务 (编程式事务) TransactionTemplate)
     * @param fromName
     * @param toName
     * @param money
     */
    public void transfer3(String fromName, String toName, Integer money) {
        transactionTemplate.execute(status -> {//开启事务
            userDao.out(fromName, money);//转出钱
            if(true) {
                throw new RuntimeException("出错了!!!");
            }
            userDao.in(toName,money);//转入钱
            return null;
        });
    }


}
