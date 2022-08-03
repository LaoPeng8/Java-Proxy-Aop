# Spring事务

> 作者: LaoPeng
>
> 2022/8/1 23:01 始

## 总结一下Spring使用事务的方式
1. 使用编程式事务
    * 注入bean事务管理器 `org.springframework.jdbc.datasource.DataSourceTransactionManager`
    * 注入bean事务管理的模板 `org.springframework.transaction.support.TransactionTemplate`
    * 然后使用@Autowired注入TransactionTemplate, 在每次需要使用的事务的方法内部使用 transactionTemplate.execute({sql语句集合(事务内容)})
    * 缺点很明显, 每次都需要使用 transactionTemplate.execute 相当麻烦, 可读性不高, 而且大量代码重复
2. 使用声明式事务 (aspectj)
    * 注入bean事务管理器 `org.springframework.jdbc.datasource.DataSourceTransactionManager`
    * 通过\<tx:method>配置需要事务的方法(可以有通配符*, 如 add* 即所有add*方法都需要事务),及隔离级别等.
    * 通过aop将 切点与切面 关联起来 (切面是通过\<tx:advice>定义的事务, 切点是需要事务的方法)
    * 好处就是 只需要一次规范的编写配置, 之后规范声明方法名 这样就可以完美的解决事务问题. 不用每个方法都配置一次事务如上面那样, 而且可读性高
3. 使用注解式事务 (@Transactional)
    * 配置类加上 @EnableTransactionManagement 开启事务, 该注解需要 `org.springframework.jdbc.datasource.DataSourceTransactionManager` (springboot貌似自动配置了)
    * 在类上使用 @Transactional, 即表示该类中所有方法开启事务, 该注解也是可以 通过参数指定 隔离级别,传播机制等
    * 在方法上使用 @Transactional, 与类上用法一致, 区别为 只为该方法开启事务
    * springboot惯用的方式, 用法简单, 不用过多配置

## Spring事务的传播特性
首先说什么是传播特性, A方法调用B方法(A与B都有@Transactional), B方法出错 则B方法肯定要回滚,那么A方法需要回滚吗? 或者 A方法出错, B方法成功执行, 则A方法肯定要回滚,那么B方法需要回滚吗?

Spring中提供了**7种事务传播特性**:
* **死活不要事务的**
   1. **Propagation.NEVER**: 如果没有,就以非事务方式执行; 如果有就抛出异常.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则调用B方法时 B方法直接抛出异常.
   2. **Propagation.NOT_SUPPORTED**: 如果没有,就以非事务方式执行; 如果有,就将当前事务挂起. 即无论如何不支持事务.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务,则B方法会将A方法的事务挂起, 然后B方法以非事务方式运行
      B方法执行结束后, A方法继续以事务方式运行. 即B方法抛出异常, A方法不会回滚(与A无关), A方法抛出异常, A方法回滚, B方法都不是事务所以不能回滚
* **可有可无的**
   3. **Propagation.SUPPORTS**: 如果没有, 就以非事务方式执行; 如果有,就使用当前事务.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就以非事务方式运行; A方法如果有事务 则B方法加入A方法的事务, 即B方法抛出异常,B方法会混滚
      A方法也会回滚, A方法抛异常 A方法回滚B方法也会回滚. (因为是同一个事务)
* **必须有事务的**
   4. **Propagation.REQUIRES_NEW**: 如果没有, 就新建一个事务; 如果有,就将当前事务挂起.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A); 如果A方法有事务,则B方法会将A方法的事务挂起
      然后B方法新建事务运行(B抛出异常会回滚,B的事务与A无关B不会影响A), B方法执行完成后, A方法继续它自己的事务. (A方法抛出异常也不会影响B,与B无关)
   5. **Propagation.NESTED**: 如果没有, 就新建一个事务; 如果有,就在当前事务中嵌套其他事务.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A); 如果A方法有事务,则B方法也会新建事务,
      但是 B方法抛出异常不影响A事务(B回滚,A不会回滚,因为B事务是不管怎么样都会新建一个事务), A方法回滚,B方法不管错没错都要回滚(因为B事务是嵌套在A事务中的),
      即 A调用B, B回滚不影响A, A回滚B一定会回滚
   6. **Propagation.REQUIRED**: 默认事务类型, 如果没有则新建一个事务; 如果有,则加入当前事务. 适合大多数情况.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就新建事务运行(B抛出异常会回滚,A不会A没有事务B不会影响A); 如果A方法有事务,则B方法加入A方法事务,
      即A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚
   7. **Propagation.MANDATORY**: 如果没有就抛出异常; 如果有就使用当前事务.
      * 解释: A方法调用B方法, A方法如果没有事务那么B方法就抛出异常;(A不回滚(A又没有事务),B不执行) 如果A方法有事务,则B方法加入当前事务;(A回滚B也回滚, B回滚A也会回滚, 要么一起回滚要么一起不滚)
  
## mysql死锁问题
在测试使用 Propagation.NOT_SUPPORTED 传播特性时, 第一次遇见了mysql死锁问题

过程: A方法调用B方法, 由于B方法传播特性为NOT_SUPPORTED, 所以A方法执行一半执行到B方法时, B方法会将A方法的事务挂起, B方法以无事务的方式运行, 等B方法执行完成后, 继续执行A方法,
但是 A方法update的事务, 将整张表都锁住了(因为 行锁在没有走索引时会转为表锁), 所以B方法根本执行不了, 需要等待A方法事务commit;后释放锁, B方法才能执行, 但是A方法又在等待B方法执行完成.
这样就A等B, B等A 就产生了死锁, 直到@Transactional设置的timeout时间到时, 抛出异常, 即结束.

原因: 产生死锁的根本就在, A方法update时 where name = ? 没有走索引, 所以行锁被转为表锁. 导致死锁.

解决: 给name字段创建一个单列索引, 或者 根据"主键"update 即 where id = ?; 可以先根据name查出id, 再根据id update (生产环境下杜绝使用非主键字段作为update where条件)
```java
//方法一, 根据主键继续update, 避免死锁
Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select id from spring_transcation where name = ?", fromName);
jdbcTemplate.update("update spring_transcation set money = money - ? where id = ?", money, (Integer) stringObjectMap.get("id"));

//方法二, 给where的name字段 加上索引
create index name_index on spring_transcation(name);
update spring_transcation set money = money + ? where name = ?
```