# Spring相关

> 作者: LaoPeng
>
> 2022/9/4 11:16 始

# springboot自动配置理解
@SpringBootApplication 主配置类
* @SpringBootConfiguration 表示该类是配置类
    * @Configuration
* @EnableAutoConfiguration 自动配置注解
    * @AutoConfigurationPackage 主程序类所在包及所有子包下的组件到扫描到spring容器中。
    * @Import({AutoConfigurationImportSelector.class}) (自动配置核心)
    * @Import 的作用是导入 该类到 ioc容器
    * AutoConfigurationImportSelector.class 自定义ImportSelector接口的实现类, 通过selectImports方法实现(方法的返回值string[] 就是要纳入IOC容器的Bean)
    * 该类中主要是通过 springFactors 机制, 从classpath中读取所有Jar包中 META-INF/spring.factories 文件
    * 并将其中 key 为 EnableAutoConfiguration.class  全类名的 value 值, 全部加载到 ioc 容器
    * 一般而言这些 value 值都是 各种jar的 xxxAutoConfiguration, 将该类加入 ioc 容器完成自动配置 (该类就会配置一些需要配置的东西)

# springmvc执行流程
1. 用户请求 dispatcherServlet(前端控制器)
2. dispatcherServlet 收到请求 并 调用 handlerMapping(处理器映射器)
3. handlerMapping 根据请求url找到具体的处理器, 生成处理器对象以及处理器拦截器(如果有则生成), 一并返回给 dispatcherServlet
4. dispatcherServlet 调用 handlerAdvice(处理器适配器)
5. handlerAdvice 执行 handler(感觉相当于就是 controller中的一个 路径方法 或者 就是controller), 并将结果 ModelAndView 返回给 dispatcherServlet
6. dispatcherServlet 将 ModelAndView 传递给 ViewResolver(视图解析器)
7. ViewResolver 解析后返回具体的 View 到 dispatcherServlet
8. dispatcherServlet 对 View 进行渲染 (也就是将 数据填充到视图中)
9. dispatcherServlet 响应用户