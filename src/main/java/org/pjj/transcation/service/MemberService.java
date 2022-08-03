package org.pjj.transcation.service;

import org.pjj.transcation.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试 事务传播特性
 * @author PengJiaJun
 * @Date 2022/08/03 00:56
 */
@Service
public class MemberService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MemberService memberService;


    //============================================= Propagation.NEVER =============================================
    /**
     * Propagation.NEVER: 如果没有,就以非事务方式执行; 如果有就抛出异常.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则调用B方法时 B方法直接抛出异常.
     *
     * 此处B方法事务传播特性为 Propagation.NEVER, 表示调用B方法的方法(A方法), 不能有事务, 如有直接抛出异常
     * 那么此处 A方法是有事务的,且为默认事务传播特性
     * 结果就是: A方法调用B方法, A本身没有错误则不会回滚, B方法不会执行(抛出异常)
     *
     * 如果此处 A方法没有事务
     * 结果就是: A方法调用B方法, A没有事务不会回滚, B没有事务不会回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A1(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        //如果B方法抛出异常, 被try catch捕获了, 则A方法不会回滚, 如果没有try catch则会回滚.
        try{
            memberService.B1(toName, money);//转入钱

//            使用this调用B1, B1的事务会失效, 大概原因就是 B1的事务是通过aop加入的, 加入后生成了代理对象,
//            this调用就相当于绕过了代理对象, 直接访问了真正的对象, 真正的对象是不具备事务的. 所以this会失效
//            解决的方法有很多种这里采用的是 将本类使用 @Autowired 注入, 然后就相当于通过代理对象调用了(没有绕过代理对象), 所以事务就生效了
//            this.B1(toName, money);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果 A 方法本身出错了, 那么也是会回滚的.
        if(false) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.NEVER: 如果没有,就以非事务方式执行; 如果有就抛出异常.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则调用B方法时 B方法直接抛出异常.
     *
     * 此处B方法事务传播特性为 Propagation.NEVER, 表示调用本方法的方法(A方法), 不能有事务, 如有直接抛出异常
     * org.springframework.transaction.IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'
     */
    @Transactional(propagation = Propagation.NEVER)
    public void B1(String toName, Integer money) {
        userDao.in(toName,money);//转入钱
        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }


    //============================================= Propagation.NOT_SUPPORTED =============================================
    /**
     * Propagation.NOT_SUPPORTED: 如果没有,就以非事务方式执行; 如果有,就将当前事务挂起. 即无论如何不支持事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务,则B方法会将A方法的事务挂起, 然后B方法以非事务方式运行
     *       B方法执行结束后, A方法继续以事务方式运行. 即B方法抛出异常, A方法不会回滚(与A无关), A方法抛出异常, A方法回滚, B方法都不是事务所以不能回滚
     *
     * 此处B方法事务传播特性为 Propagation.NOT_SUPPORTED, 表示B方法无论文化不可能有事务
     * 那么此处 A方法是有事务的,且为默认事务传播特性
     * 结果就是: A方法调用B方法, A本身没有错误则不会回滚, B方法没有事务,无论对错都不会回滚
     *
     * 如果此处 A方法没有事务
     * 结果就是: A方法调用B方法, A没有事务不会回滚, B没有事务不会回滚
     *
     * 使用该Propagation.NOT_SUPPORTED传播特性时, 第一次遇见mysql死锁问题. 即两个方法都不执行(被锁了),一直等着超时了.
     * 死锁问题在 spring事务.md 中详细解释.
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A2(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        //如果B方法抛出异常, 被try catch捕获了, 则A方法不会回滚, 如果没有try catch则会回滚.
        try{
            memberService.B2(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果 A 方法本身出错了, 那么也是会回滚的.
        if(true) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.NOT_SUPPORTED: 如果没有,就以非事务方式执行; 如果有,就将当前事务挂起. 即无论如何不支持事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务,则B方法会将A方法的事务挂起, 然后B方法以非事务方式运行
     *       B方法执行结束后, A方法继续以事务方式运行. 即B方法抛出异常, A方法不会回滚(与A无关), A方法抛出异常, A方法回滚, B方法都不是事务所以不能回滚
     *
     * 此处B方法事务传播特性为 Propagation.NOT_SUPPORTED, 表示调用本方法的方法(A方法), 如果没有事务, 则本方法也没有事务,
     * 如果A方法有事务, 则B方法执行期间会将A方法事务挂起, 然后 B方法继续以 无事务的状态运行
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void B2(String toName, Integer money) {
        userDao.in(toName,money);//转入钱
        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }


    //============================================= Propagation.SUPPORTS =============================================
    /**
     * Propagation.SUPPORTS: 如果没有, 就以非事务方式执行; 如果有,就使用当前事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则B方法加入A方法的事务, 即B方法抛出异常,B方法会混滚 A方法也会回滚,
     * A方法抛异常 A方法回滚B方法也会回滚. (因为是同一个事务)
     *
     */
//    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A3(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        //就算捕获了 B方法抛出的异常, A方法也是会回滚的. 因为A与B是同一个事务 B回滚了则表示A也回滚了
        try{
            memberService.B3(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(false) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.SUPPORTS: 如果没有, 就以非事务方式执行; 如果有,就使用当前事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则B方法加入A方法的事务, 即B方法抛出异常,B方法会混滚 A方法也会回滚,
     * A方法抛异常 A方法回滚B方法也会回滚. (因为是同一个事务)
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void B3(String toName, Integer money) {
        userDao.in(toName,money);//转入钱
        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }


    //============================================= Propagation.REQUIRES_NEW =============================================
    /**
     * Propagation.REQUIRES_NEW: 如果没有, 就新建一个事务; 如果有,就将当前事务挂起.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A);
     * 如果A方法有事务,则B方法会将A方法的事务挂起 然后B方法新建事务运行(B抛出异常会回滚,B的事务与A无关B不会影响A),
     * B方法执行完成后, A方法继续它自己的事务. (A方法抛出异常也不会影响B,与B无关)
     *
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A4(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        try{
            memberService.B4(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(false) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.REQUIRES_NEW: 如果没有, 就新建一个事务; 如果有,就将当前事务挂起.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A);
     * 如果A方法有事务,则B方法会将A方法的事务挂起 然后B方法新建事务运行(B抛出异常会回滚,B的事务与A无关B不会影响A),
     * B方法执行完成后, A方法继续它自己的事务. (A方法抛出异常也不会影响B,与B无关)
     *
     * 此处B方法事务传播特性为 Propagation.REQUIRES_NEW, 表示调用本方法的方法(A方法), 无论是否有事务, 本方法都会新建一个事务,
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void B4(String toName, Integer money) {
        userDao.in(toName,money);//转入钱
        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }


    //============================================= Propagation.NESTED =============================================
    /**
     * Propagation.NESTED: 如果没有, 就新建一个事务; 如果有,就在当前事务中嵌套其他事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A); 如果A方法有事务,则B方法也会新建事务,
     *       但是 B方法抛出异常不影响A事务(B回滚,A不会回滚,因为B事务是不管怎么样都会新建一个事务), A方法回滚,B方法不管错没错都要回滚(因为B事务是嵌套在A事务中的),
     *       即 A调用B, B回滚不影响A, A回滚B一定会回滚
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A5(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        try{
            memberService.B5(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        //A出错回滚, 不管B是出错, A只要回滚B也要回滚
        if(true) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.NESTED: 如果没有, 就新建一个事务; 如果有,就在当前事务中嵌套其他事务.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A); 如果A方法有事务,则B方法也会新建事务,
     *       但是 B方法抛出异常不影响A事务(B回滚,A不会回滚,因为B事务是不管怎么样都会新建一个事务), A方法回滚,B方法不管错没错都要回滚(因为B事务是嵌套在A事务中的),
     *       即 A调用B, B回滚不影响A, A回滚B一定会回滚
     */
    @Transactional(propagation = Propagation.NESTED)
    public void B5(String toName, Integer money) {
        userDao.in(toName,money);//转入钱
        if(false) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }


    //============================================= Propagation.REQUIRED =============================================
    /**
     * Propagation.REQUIRED: 默认事务类型, 如果没有则新建一个事务; 如果有,则加入当前事务. 适合大多数情况.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A);
     * 如果A方法有事务,则B方法加入A方法事务, 即A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚
     *
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)//默认事务传播特性
    public void A6(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        try{
            memberService.B6(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        //A没错, B抛出异常 A也会回滚 (因为 A与B是同一个事务)
        if(false) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.REQUIRED: 默认事务类型, 如果没有则新建一个事务; 如果有,则加入当前事务. 适合大多数情况.
     * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A);
     * 如果A方法有事务,则B方法加入A方法事务, 即A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚
     *
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void B6(String toName, Integer money) {
        userDao.in(toName,money);//转入钱

        //B没错, A抛出异常 B也会回滚 (因为 A与B是同一个事务)
        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }



    //============================================= Propagation.MANDATORY =============================================
    /**
     * Propagation.MANDATORY: 如果没有就抛出异常; 如果有就使用当前事务.
     * A方法调用B方法, A方法如果没有事务那么B方法就抛出异常;(A不回滚(A又没有事务),B不执行)
     * 如果A方法有事务,则B方法加入当前事务;(A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚)
     *
     *
     */
//    @Transactional(propagation = Propagation.REQUIRED)//没有事务
    public void A7(String fromName, String toName, Integer money) {
        userDao.out(fromName, money);//转出钱

        try{
            memberService.B7(toName, money);//转入钱
        } catch (Exception e) {
            e.printStackTrace();
        }

        //A不会回滚(A没有事务), B不执行 (A没有事务, 调用B方法 B方法直接抛出异常)
        if(false) {
            throw new RuntimeException("A方法出错了!!!");
        }
    }

    /**
     * Propagation.MANDATORY: 如果没有就抛出异常; 如果有就使用当前事务.
     * A方法调用B方法, A方法如果没有事务那么B方法就抛出异常;(A回滚,B不执行)
     * 如果A方法有事务,则B方法加入当前事务;(A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚)
     *
     * 此处B方法事务传播特性为 Propagation.MANDATORY, 表示调用本方法的方法(A方法), 必须要有事务, 如果没有事务则B方法直接抛出异常:
     * org.springframework.transaction.IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void B7(String toName, Integer money) {
        userDao.in(toName,money);//转入钱

        if(true) {
            throw new RuntimeException("B方法出错了!!!");
        }
    }

}
