# Spring事务

> 作者: LaoPeng
>
> 2022/8/1 23:01 始

总结一下Spring使用事务得方式
1. 使用编程式事务
    * 注入bean事务管理器 `org.springframework.jdbc.datasource.DataSourceTransactionManager`
    * 注入bean事务管理的模板 `org.springframework.transaction.support.TransactionTemplate`
    * 然后使用@Autowired注入TransactionTemplate, 在每次需要使用的事务的方法内部使用 transactionTemplate.execute({sql语句集合(事务内容)})
    * 缺点很明显, 每次都需要使用 transactionTemplate.execute 相当麻烦, 可读性不高, 而且大量代码重复
2. 使用声明式事务 (aspectj)
    * 注入bean事务管理器 `org.springframework.jdbc.datasource.DataSourceTransactionManager`
    * 通过\<tx:method>配置需要事务的方法(可以有通配符*, 如 add* 即所有add*方法都需要事务),及隔离级别等.
    * 通过aop将 切点与切面 关联起来 (切面是通过\<tx:advice>定义的事务, 切点是需要事务的方法)
    * 好处就是 只需要一次规范的编写配置, 之后规范的生命方法名 这样就可以完美的解决事务问题. 不用每个方法都配置一次事务如上面那样, 而且可读性高
3. 使用注解式事务 (@Transactional)
    * 配置类加上 @EnableTransactionManagement 开启事务, 该注解需要 `org.springframework.jdbc.datasource.DataSourceTransactionManager` (springboot貌似自动配置了)
    * 在类上使用 @Transactional, 即表示该类中所有方法开启事务, 该注解也是可以 通过参数指定 隔离级别,传播机制等
    * 在方法上使用 @Transactional, 与类上用法一致, 区别为 只为该方法开启事务
    * springboot惯用的方式, 用法简单, 不用过多配置