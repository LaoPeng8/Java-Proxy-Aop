# Java笔试面试集合

*前言: 前几天一家公司要先笔试后面试, 笔试都没过, 太坑了, 好多都是一眼看过去非常熟悉但是就是不确定哪些是100%正确的, 所以刷题是非常有必要的*

> 作者: LaoPeng
>
> 2024/8/12 17:36 始

### 1.JDK、JRE、JVM之间的区别
* JDK(Java SE Development Kit), Java标准开发包, 它提供了编译、运行Java程序所需的各种工具和资源, 包括Java编译器、Java运行时环境, 以及常用的Java类库等
* JRE(Java Runtime Environment), Java运行环境, 用于运行Java的字节码文件. JRE中包括了JVM以及JVM工作所需要的类库, 普通用户只需要安装JRE来运行Java程序, 而程序开发者必须安装JDK来编译、调试程序. 
* JVM(Java Virtual Mechinal),  Java虚拟机, 是JRE的一部分, 它是整个java实现跨平台的最核心的部分, 负责运行字节码文件.  

我们写Jlava代码, 用xt就可以写, 但是写出来的Java代码, 想要运行, 需要先编译成字节码, 那就需要编泽器, 而JDK中就包含了编泽器javac, 编译之后的字节码, 想要运行, 就需要一个可以执行字节码的程序, 这个程序就是VM (Java虚拟机), 专门用来执行Java字节码的. 

如果我们要开发Java程序, 那就需要JDK, 因为要编译Java源文件. 

如果我们只想运行已经编译好的Java字节码文件, 也就是*.class文件, 那么就只需要JRE. 

JDK中包含了JRE, JRE中包含了JVM. 

另外, JVM在执行Java字节码时, 需要把字节码解释为机器指令, 而不同操作系统的机器指令是有可能不一样的, 所以就导致不同操作系统上的JVM是不一样的, 所以我们在安装JDK时需要选择操作系统. 

另外, NM是用来执行Java字节码的, 所以凡是某个代码编译之后是Java字节码, 那就都能在JVM上运行, 比如Apache Groovy Scala and Kotlin等等. 

### 2.hashCode()与equals()之间的关系
在Java中, 每个对象都可以调用自己的hashCode()方法得到自己的哈希值(hashCode), 相当于对象的指纹信息, 通常来说世界上没有完全相同的两个指纹, 但是在Java中做不到这么绝对, 但是我们仍然可以利用hashCode来做一些提前的判断, 比如:
* 如果两个对象的hashCode不相同, 那么这两个对象肯定是不同的两个对象
* 如果两个对象的hashCode相同, 不代表这两个对象一定是同一个对象, 也可能是两个对象
* 如果两个对象相等, 那么他们的hashCode就一定相同

在Java的一些集合类的实现中, 在比较两个对象是否相等时, 会根据上面的原则, 会先调用对象的hashCode()方法得到hashCode进行比较, 如果hashCode不相同, 就可以直接认为这两个对象不相同, 如果hashCode相同, 那么就会进一步调用equals()方法进行比较. 而equals()方法, 就是用来最终确定两个对象是不是相等的, 通常equals方法的实现会比较重, 逻辑比较多, 而hashCode()主要就是得到一个哈希值, 实际上就一个数字, 相对而言比较轻, 所以在比较两个对象时, 通常都会先根据hashCode想比较一下.

所以我们就需要注意, 如果我们重写了equals()方法, 那么就要注意hashCode()方法, 一定要保证能遵守上述规则. 

```java
/**
 * 实验发现:
 *
 * HashSet存放相同的对象时是会被覆盖的, 也就是相同的对象对象只能有一个, 但是此处却出现了两个 {"张三", 18}
 * 因为在Java中所有的关于Hash的集合都是先比较hashcode, 当hashcode一致时才会继续比较, 引用地址值或equals方法来判断对象是否重复 (equals方法实现重, hashcode方法实现轻)
 *
 * 表面上我们为了判断User对象是否一致重写了equals()方法, 目的就是为了让两个User对象一致, 如果在不使用Hash相关实现的集合啥的, 应该是没有问题的.
 * 但是在使用Hash相关实现时, 并不是只看equals方法, 所以只重写equals方法是不行的.
 *
 * @author PengJiaJun
 * @Date 2024/08/12 18:01
 */
public class Test {
    public static void main(String[] args) {
        HashSet<User> hashSet = new HashSet<>();

        hashSet.add(new User("张三", 18));
        hashSet.add(new User("张三", 18));

        hashSet.stream().forEach(System.out::println);
    }
}

class User {

    private String name;
    private Integer age;

    public User() {
    }
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}' +
                " + name = " + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.name.equals(user.name) && this.age.equals(user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### 3.String、StringBuffer、StringBuilder的区别
1. String是不可变的, 如果尝试去修改, 会新生成一个字符串对象, StringBuffer, StringBuilder是可变的. 通过append()方法增加字符串
2. StringBuffer线程安全, StringBuilder线程不安全, 所以单线程环境下StringBuilder效率更高

### 4.泛型中extends和super的区别
1. <? extends T> 表示包括T在内的任何T的子类
2. <? super T> 表示包括T在内的任何T的父类
3. Java泛型只在编译期存在效果

### 5.==和equals方法的区别
* ==: 如果是基本数据类型, 比较的是值, 如果是引用类型, 比较的是引用地址
* equals: 具体看各个类重写equals方法后的比较逻辑, 比如String类, 虽然是引用类型, 但是重写equals方法后是比较的每个字符是否全部相等

### 6.Spring中Bean是线程安全的吗
Spring本身并没有针对Bean做线程安全的处理, 所以:
1. 如果Bean是无状态的, 那么Bean则是线程安全的
2. 如果Bean是有状态的, 那么Bean则不是线程安全的
另外, Bean是不是线程安全, 跟Bean本身的作用域没有关系, Bean的作用域只是表示Bean的生命周期范围, 对于任何生命周期的Bean都是一个对象, 这个对象是否线程安全还得看这个Bean对象本身.

### 7.ApplicationContext和BeanFactory有什么区别
BeanFactory是Spring中非常核心的组件, 表示Bean工厂, 可以生成Bean, 维护Bean, 而ApplicationContext继承了BeanFactory, 
所以ApplicationContext拥有BeanFactory所有的特点, 也是一个Bean工厂, 但是ApplicationContext除开继承了BeanFactory之外, 
还继承了诸如EnvironmentCapable、MessageSource、ApplicationEventPublisher等接口, 从而ApplicationContext还有获取系统环境变量、国际化、事件发布等功能, 这是BeanFactory所不具备的

### 8.重载和重写的区别
* 重载(Overload):在一个类中, 同名的方法如果有不同的参数列表(比如参数类型不同、参数个数不同)则视为重载.
* 重写(Override):从字面上看, 重写就是重新写一遍的意思. 其实就是在子类中把父类本身有的方法重新写一遍. 子类继承了父类的方法, 但有时子类并不想原封不动的继承父类中的某个方法, 所以在方法名, 参数列表, 返回类型都相同(子类中方法的返回值可以是父类中方法返回值的子类)的情况下, 对方法体进行修改, 这就是重写. 但要注意子类方法的访问修饰权限不能小于父类的.

### 9.List和Set的区别
* List: 有序, 按对象插入的顺序保存对象, 可重复, 允许多个Null元素对象, 可以使用Iterator取出所有元素, 在逐一遍历, 还可以使用get(int index)获取指定下标的元素
* Set: 无序, 不可重复, 最多允许有一个Null元素对象, 取元素时只能用Iterator接口取得所有元素, 再逐一遍历各个元素

### 10.ArrayList和LinkedList区别
1. 首先, 他们的底层数据结构不同, ArrayList底层是基于数组实现的, LinkedList底层是基于链表实现的
2. 由于底层数据结构不同, 他们所适用的场景也不同, ArrayList更适合随机查找,LinkedList更适合删除和添加, 查询、添加、删除的时间复杂度不同
3. 另外ArrayList和LinkedList都实现了List接口, 但是LinkedList还额外实现了Deque接口, 所以LinkedList还可以当做队列来使用

### 11.B树和B+树的区别, 为什么MySQL使用B+树
B树的特点:
1. 节点排序
2. 一个节点可以存多个元素, 多个元素也排序了 (参考234树)

B+树特点:
1. 拥有B树的特点
2. 叶子节点之间有指针
3. 非叶子节点上的元素在叶子节点上都冗余了, 也就是叶子节点中存储了所有的元素, 并且排好顺序

MySQL索引使用的是B+树, 因为索引是用来加快查询的, 而B+树通过对数据进行排序所以是可以提高查询速度的, 然后通过一个节点中可以存储多个元素, 从而可以使得B+树的高度不会太高, 在Mysql中一个Innodb页就是一个B+树节点, 一个innodb页默认16kb, 所以一般情况下一颗两层的B+树可以存200万行左右的数据, 然后通过利用B+树叶子节点存储了所有数据并且进行了排序, 并且叶子节点之间有指针, 可以很好的支持全表扫描, 范围查找等SQL语句

### 12.CopyOnWriteArrayList的底层原理是怎样的
1. 首先CopyOnWriteArraylist内部也是用过数组来实现的, 在向CopyOnWriteArrayList添加元素时, 会复制一个新的数组, 写操作在新数组上进行, 读操作在原数组上进行
2. 并且, 写操作会加锁, 防止出现并发写入丢失数据的问题
3. 写操作结束之后会把原数组指向新数组
4. CopyOnWriteArraylist允许在写操作时来读取数据, 大大提高了读的性能, 因此适合读多写少的应用场景, 但是CopyOnWriteArrayList会比较占内存, 同时可能读到的数据不是实时最新的数据, 所以不适合实时性要求很高的场景

### 13.Java中的异常体系是怎样的
* Java中的所有异常都来自顶级父类Throwable.
* Throwable下有两个子类Exception和Error.
* Error表示非常严亚的错误, 比如java.lang StackOverFlowError和java.lang.OutOfMemoryEFor, 还常这些错误出现时, 仅仅捂掌程序自己是解决不了的, 可能是虚拟机、磁盘、操作系统层面出现的问题了, 所以通常也不建议在代码中去捕获这些Error, 因为搏获的意义不大, 因为程序可能已经根本运行不了了.
* Exception表示异常, 表示程序出现Exception时, 是可以靠程序自己来解决的, 比如NullPointerException IllegalAccessException等, 我们可以楠获这些异常来做特殊处理.
* Exception的子类通常又可以分为RuntimeException和非RuntimeException两类
* RunTimeException表示运行期异常, 表示这个异常是在代码运行过程中抛出的, 这些孕常是非桧查异常, 程序中可以选择捕获处理, 也可以不处理.这些异常一般是由程序逻辑错误引起的, 程序应该从逻辐角度尽可能避免这类异常的发生, 比如NullPointerException、IndexOutOfRoundsException等.
* 非RuntimeException表示非运行期异常, 也就是我们常说的检查异常, 是必须进行处理的奔常, 如果不处理, 租序就不能检童票常通过.如IOException、SQLException等以及用户自定义的Exception异常.

### 14.Java中有哪些类加载器
JDK自带有三个类加载器: BootstrapClassLoader, ExtClassLoader, AppClassLoader.
* BootstrapClassLoader是ExtClassLoader的父类加载器, 默认负责加载%JAVA_HOME%/lib下的jar包和class文件
* ExtClassLoader是AppClassLoader的父类加载器, 负责加载%JAVA_HOME%/lib/ext文件夹下的jar包和class类
* AppClassLoader是自定义类加载器的父类, 负责加载classpath下的类文件

### 15.说说累加载器的双亲委派模型
JVM中存在三个默认的类加载器
1. BootstrapClassLoader
2. ExtClassLoader
3. AppClassLoader

AppClassLoader的父加载器是ExtClassLoader, ExtClassLoader的父加载器是BootstrapClassLoader.

JMM在加载一个类时, 会调用AppClassLoader的loadClass方法来加载这个类, 不过在这个方法中, 会先使用ExtClassloader的loadClass方法来加载类, 同样ExtClassloader的loadClass方法中会先使用BootstrapClassloader来加载类, 如果BootstrapClassLoader加载到了就直接成功, 如果BootstrapClassloader没有加载到, 那么ExtClassLoader就会自己尝试加载该类, 如果没有加载到, 那么则会由AppClassLoader来加载这个类.

所以, 双亲委派指得是, JVM在加载类时, 会委派给Ext和Bootstrap进行加载, 如果没加载到才由自己进行加载.

其中有安全性的考虑, 例如加载一个我们自己编写的 java.lang.String 由于双亲委派的存在, 就算由我们自定义的类加载器加载, 实际上也会一层一层交给父类处理.

### 16.JDK1.7到JDK1.8 HashMap发生了什么变化(底层)
1. 1.7中底层是数组+链表, 1.8中底层是数组+链表+红黑树, 加红黑树的目的是提高HashMap插入踟查询整体效率
2. 1.7中链表插入使用的是头插法, 1.8中链表插入使用的是尾插法, 因为1.8中插入key和value时需要判断链表元素个数, 所以需要遍历链表统计链表元素个数, 所以正好就直接使用尾插法
3. 1.7中哈希算法比较复杂, 存在各种右移与异或运算, 1.8中进行了简化, 因为复杂的哈希算法的目的就是提高散列性, 来提供HashMap的整体效率, 而1.8中新增了红黑树, 所以可以适当的简化哈希算法, 节省CPU资源

### 17.JVM有哪些垃圾回收算法
1. 标记清除算法:
   1. 标记阶段: 把垃圾内存标记出来
   2. 清除阶段:直接将垃圾内存回收
   3. 这种算法是比较简单的, 但是有个很严重的问题, 就是会产生大量的内存碎片
2. 复制算法: 为了解决标记清除算法的内存碎片问题, 就产生了复制算法. 复制算法将内存分为大小相等的两半, 每次只使用其中一半.垃圾回收时, 将当前这一块的存活对象全部拷贝到另-半, 然后当前这一半内存就可以直接清涂.这种算法没有内存碎片, 但是他的问题就在于浪费空间.而且, 他的效率跟存活对象的个数有关. 
3. 标记压缩算法: 为了解决复制算法的缺陷, 就提出了标记压缩算法.这种算法在标记阶段跟标记清除算法是一样的, 但是在完成标记之后, 不是直接清理垃圾内存, 而是将存活对象往一端移动, 然后将边界以外的所有内存直接清除.

### 18.MyBatis优缺点
优点:
1. 基于SQL语句编程, 相当灵活, 不会对应用程序或者数据库的现有设计造成任何影响, SQL写在XML里, 解除sql与程序代码的耦合, 便于统一管理;提供XML标签, 支持编写动态SQL语句, 并可重用.
2. 与JDBC相比, 减少了50%以上的代码量, 消除了JDBC大量冗余的代码, 不需要手动开关连接;
3. 很好的与各种数据库兼容(因为MyBatis 使用JDBC来连接数据库, 所以只要JDBC支持的数据库MyBatis都支持)
4. 能够与Spring 很好的集成;
5．提供映射标签, 支持对象与数据库的ORM字段关系映射;提供对象关系映射标签, 支持对象关系组件维护.

缺点:
1. SQL语句的编写工作量较大, 尤其当字段多、关联表多时, 对开发人员编写SQL语句的功底有一定要求.
2. SQL语句依赖于数据库, 导致数据库移植性差, 不能随意更换数据库.

### 19.Mybatis中 #{}和${}的区别是什么
`#{}是预编译处理, 是占位符`
`${}是字符串替换, 是拼接符`

MyBatis在处理#{}时, 会将SQL中的#{}替换成?号, 调用PreparedStatement来赋值;

MyBatis在处理${}时, 会将SQL中的${}替换成变量的值, 调用Statement来赋值;

使用#{}可以有效的防止SQL注入, 提高系统安全性

### 20.MySQL慢查询如何优化
* 检查是否走了索引, 如果没有则优化SQL利用索引
* 检查所利用的索引, 是否是最优索引
* 检查所查字段是否都是必须的, 是否查询了过多字段, 查出了多余数据
* 检查表中数据是否过多, 是否应该进行分库分表了
* 检查数据库实例所在机器的性能配置, 是否太低, 是否可以适当增加资源

### 21.MySQL锁有哪些, 如何理解
按锁粒度分类:
1. 行锁: 锁某行数据, 锁粒度最小, 并发度高
2．表锁:锁整张表, 锁粒度最大, 并发度低
3．间隙锁:锁的是一个区间

还可以分为:
1. 共享锁: 也就是读锁, 一个事务给某行数据加了读锁, 其他事务也可以读, 但是不能写
2. 排它锁: 也就是写锁, 一个事务给某行数据加了写锁, 其他事务不能读, 也不能写

还可以分为:
1. 乐观锁: 并不会真正的去锁某行记录, 而是通过一个版本号来实现的
2. 悲观锁: 上面所的行锁、表锁等都是悲观锁

在事务的隔离级别实现中, 就需要利用锁来解决幻读

### 22.Redis和MySQL如何保证数据一致
1. 先更新Mysql, 再更新Redis, 如果更新Redis失败, 可能仍然不一致
2. 先删除Redis缓存数据, 再更新Mysql, 再次查询的时候在将数据添加到缓存中, 这种方案能解决1方案的问题, 但是在高并发下性能较低, 而且仍然会出现数据不一致的问题, 比如线程1删除了Redis缓存数据, 正在更新Mysql, 此时另外一个查询再查询, 那么就会把Mysql中老数据又查到Redis中
3. 延时双删, 步骤是:先删除Redis缓存数据, 再更新Mysql, 延迟几百毫秒再删除Redis缓存数据, 这样就算在更新Mysql时, 有其他线程读了Mysql), 把老数据读到了Redis中, 那么也会被删除掉, 从而把数据保持一致

### 23.Redis有哪些数据结构? 分别有哪些应用场景
1. 字符串: 可以用来做最简单的数据, 可以缓存某个简单的字符串, 也可以缓存某个json格式的字符串, Redis分布式锁的实现就利用了这种数据结构, 还包括可以实现计数器、Session共享、分布式ID
2. 哈希表: 可以用来存储一些key-value对, 更适合用来存储对象
3. 列表: Redis的列表通过命令的组合, 既可以当做栈, 也可以当做队列来使用, 可以用来缓存类似微信公众号、微博等消息流数据
4. 集合: 和列表类似, 也可以存储多个元素, 但是不能重复, 集合可以进行交集、并集、差集操作, 从而可以实现类似, 我和某人共同关注的人、朋友圈点赞等功能
5. 有序集合: 集合是无序的, 有序集合可以设置顺序, 可以用来实现排行榜功能

### 24.ReentrantLock中tryLock()和Lock()方法的区别
1. tryLock()表示尝试加锁, 可能加到, 也可能加不到, 该方法不会阻塞线程, 如果加到锁则返回true, 没有加到则返回false
2. lock()表示阻塞加锁, 线程会阻塞直到加到锁, 方法也没有返回值

### 25.SOA, 分布式, 微服务之间的关系和区别
1. 分布式架构是指将单体架构中的各个部分拆分, 然后部署到不同的机器或进程中去, SOA和微服务基本上都是分布式架构的
2. SOA是一种面向服务的架构, 系统的所有服务都注册在总线上, 当调用服务时, 从总线上查找服务信息, 然后调用 (WebService调用的这种方式)
3. 微服务是一种更彻底的面向服务的架构, 将系统中各个功能个体抽成一个个小的应用程序, 基本保持一个应用对应的一个服务的架构

### 26.SpringBoot是如何启动Tomcat的
1. 首先SpringBoot在启动时会创建一个Spring容器
2. 在创建Spring容器过程中, 会利用@ConditionOnClass注解来判断当前classpath中是否存在Tomcat依赖, 如果存在则会生成一个启动Tomcat的bean
3. Spring容器创建完成之后, 就会获取启动Tomcat的Bean, 并创建Tomcat对象, 并绑定端口等, 然后启动Tomcat

### 27.SpringBoot中常用注解及其底层实现
1. @SpringBootApplication注解: 这个注解标识了一个SpringBoot工程, 是SpringBoot主启动类注解, 它实际上由另外3个注解组成, 分别为:
   * @SpringBootConfiguration注解: 这个注解实际上就是@Configuration, 表示启动类也是一个配置类 (和普通的配置类有所区分)
   * @EnableAutoConfiguration注解: 这个注解向Spring容器中导入了一个Selector, 用来加载classpath下SpringFactories中定义的自动配置类, 将这些类注册成bean
   * @ComponentScan注解: 这个注解标识扫描路径, 因为默认是没有配置实际扫描路径的, 所以SpringBoot扫描主启动类所在的当前目录及其子目录, 就是因为@SpringBootApplication注解下有该注解
2. @Bean注解: 用来定义bean, 类似于XML中的\<bean\>标签, Spring在启动时, 会对加了@Bean注解的方法进行解析, 将方法名字作为beanName, 并通过执行方法得到bean对象
3. @Controller, @Service, @ResponseBody, @Autowired都可以说

### 28.SpringCloud和Dubbo有哪些区别
SpringCloud是一个微服务框架, 提供了微服务领域中的很多功能组件, Dubbo—开始是一个RPC调用框架, 核心是解决服务调用间的问题, SpringCloud是一个大而全的框架, Dubbo则更侧重于服务调用, 所以Dubbo所提供的功能没有SpringCloud全面, 但是Dubbo的服务调用性能比SpringCloud高, 不过SpringCloud和Dubbo并不是对立的, 是可以结合起来一起使用的.

### 29.SpringCloud中有哪些常用组件
* Eureka: 注册中心
* Nacos: 注册中心、配置中心
* Consul: 注册中心、配置中心
* SpringCloud Config: 注册中心、配置中心 (以上四个组件基本一致, 一般选择一种使用)
* Feign/OpenFeign: RPC调用
* Kong: 服务网关
* Zuul: 服务网关
* SpringCloud Gateway: 服务网关 (以上三个组件(网关)基本一致, 一般选择一种使用)
* Ribbon: 负载均衡
* SpringCloud Sleuth: 链路追踪
* Zipkin: 链路追踪
* Seata: 分布式事物
* Dubbo: RPC调用
* Sentinel: 服务熔断
* Hystrix: 服务调用

### 30.什么是服务雪崩, 什么是服务限流
1. 当服务A调用服务B, 服务B调用服务C, 此时大量请求突然请求服务A, 假设服务A本身是可以扛住这些请求的, 但如果服务C扛不住, 导致服务C请求堆积, 从而导致服务B请求堆积, 从而服务A不可用, 这就是服务雪崩, 解决方式是服务降级和服务熔断
2. 服务限流是指在高并发请求下, 为了保护系统, 可以对访问服务的请求进行数量上的限制, 从而防止系统不被大量请求压垮, 在秒杀中限流是非常重要的

### 31.什么是服务熔断, 什么是服务降级, 区别是什么
1. 服务熔断是指, 当服务A调用的某个服务B不可用时, 上游服务A为了保证自己的业务不受影响, 从而不在调用服务B, 直接返回一个结果, 减轻服务A和服务B的压力, 直到服务B恢复
2. 服务降级是指, 当发现系统压力过大时, 可以通过关闭某个服务, 或限制某个服务来减轻系统压力, 这就是服务降级

### 32.Spring事物传播机制
多个事务方法相互调用时, 事务如何在这些方法间传播, 方法A是一个事务的方法, 方体A执行过程中调用了方法B, 那么方法B有无事务以及方法B对事务的要求不同都会对方法A的事务具体执行造成影响, 同时方法A的事务对方法B的事务执行也有影响, 这种影响具体是什么就由两个方法所定义的事务传播类型所决定
1. REQUIRED(默认): 如果当前没有事务, 则自己新建一个事务, 如果当前存在事务, 则加入该事务
2. SUPPORTS: 当前存在事务, 则加入当前事务, 如果当前没有事务, 就以非事务方法执行
3. MANDATORY: 当前存在事务, 则加入当前事务, 如果当前事务不存在(没有事务), 则抛出异常
4. REQUIRES_NEW: 新建一个事务, 如果当前存在事务, 则挂起当前事务
5. NOT_SUPPORTED: 以非事务方式执行, 如果当前存在事务, 则挂起当前事务
6. NEVER: 不使用事务, 如果当前事务存在则抛出异常
7. NESTED: 如果当前事务存在, 则在嵌套事务中执行, 否则REQUIRED的操作一样.

### 33.Spring事务什么时候会失效
Spring事务的原理是AOP, 进行了切面增强, 那么失效的根本原因就是这个AOP不起作用了, 常见情况如下:
1. 发生自调用, 类里面使用this调用本类的方法(this通常省略), 此时这个this不是代理类, 而是Service本身, 解决方法很简单, 让this变成代理类即可, 可以通过@Autowried注入一个本类的bean, 这个注入的bean就是本类的代理类
2. 方法不是public的, @Transaction只能作用于public方法上(Spring内部实现时检查了方法修饰符, 非public修饰的方法可能无法被外部调用, 而事务通常需要在外部调用的方法中开启. 如果方法无法被外部调用, 事务就无法生效), 否则事务不会失效, 如果要用在非public方法上, 可以开启AspectJ代理模式
3. 方法被final修饰, 那么代理类无法重写该方法, static方法也不可以
4. 数据库不支持事务
5. 方法上加上了@Transctional注解, 但类没有被Spring管理
6. 异常被吃掉, 事务不会回滚(Spring事务只支持未检查异常(Error与RuntimeException与我们项目中会自定义异常继承自RuntimeException类), Throwable与Exception属于已检查异常, Spring默认不会回滚, 需要使用`rollbackFor = Exception.class`指定Spring支持的异常类型)

### 34.Spring中用到了哪些设计模式
1. 工厂模式: BeanFactory, FactoryBean, ProxyFactory
2. 原型模式: 原型Bean, PrototypeTargetSource, PrototypeAspectInstanceFactory
3. 单例模式: 单例Bean, SingletonTargetSource, DefaultBeanNameGenerator, SimpleAutowireCandidateResolver, AnnotationAwareOrderComparator
4. 构造器模式: BeanDefinitionBuilder - BeanDefinition构造器, BeanFactoryAspectJAdvisorsBuilder - 解析并构造@AspectJ注解的Bean中所定义的Advisor, StringBuilder
5. 适配器模式: ApplicationListenerMethodAdapter - 将@EventListener注解的方法适配成ApplicationListener, AdvisorAdapter - 把Advisor适配成MethodInterceptor
6. 访问者模式: PropertyAccessor - 属性访问器, 用来访问和设置某个对象的某个属性, MessageSourceAccessor - 国际化资源访问器
7. 装饰器模式: BeanWrapper - 比单纯的Bean对象功能更加强大, HttpRequestWrapper
8. 代理模式: AOP, @Configuration, @Lazy
9. 观察者模式: ApplicationListener - 事件监听机制, AdvisedSupportListener - ProxyFactory可以提交此监听器, 用来监听ProxyFactory创建代理对象完成事件、添加Advisor事件等
10. 策略模式: InstantiationStrategy - Spring需要根据BeanDefinition来实例化Bean, 但是可以根据不同的策略来实例化, BeanNameGenerator - beanName生成器
11. 模板方法模式: AbstractApplicationContext: postProcessBeanFactory() - 子类可以继续处理BeanFactory, onRefresh() - 子类可以做一些额外的初始化
12. 责任链模式: DefaultAdvisorChainFactory - 负责构建一条AdvisorChain, 代理对象执行某个方法时会依次经过AdvisorChain中的每个Advisor, QualifierAnnotationAutowireCandidateResolver - 判断某个bean能不能用来进行依赖注入(勉强可以认为是责任链)

### 35.Spring中的事务是如何实现的
1. Spring事务底层是基于数据库事务和AOP机制的
2. 首先对于使用了@Transactional注解的bean, Spring会创建一个代理对象作为bean
3. 当调用代理对象的方法时, 会先判断该方法上是否加上了@Transcational注解
4. 如果加了, 那么则使用事务管理器创建一个数据库连接
5. 并且修改数据库连接的autocommit属性为false, 禁止此连接的自动提交, 这是实现Spring事务非常重要的一步
6. 然后执行当前方法, 方法中会执行SQL
7. 当执行完方法后, 如果没有出现异常则世界提交事务
8. 如果出现了异常, 并且这个异常是需要回滚的则回滚事务, 否则仍然提交事务
9. Spring事务的隔离级别对应的就是数据库的隔离级别
10. Spring事务的传播机制是Spring事务自己实现的, 也是Spring事务中最复杂的
11. Spring事务的传播机制是基于数据库连接来做的, 一个数据库连接一个事务, 如果传播机制配置为需要新开一个事务, 那么实际上就是先建立一个数据库连接, 在此新数据库连接上执行SQL

### 36.Synchronized的偏向锁, 轻量级锁, 重量级锁
1. 偏向锁: 在锁对象的对象头中记录一下当前获取到该锁的线程ID, 该线程下次如果又来获取该锁就可以直接获取到了
2. 轻量级锁: 由偏向锁升级而来, 当一个线程获取到锁后, 此时这把锁是偏向锁, 此时如果有第二个线程来竞争锁, 偏向锁就会升级为轻量级锁, 之所以叫轻量级锁, 是为了和重量级锁区分开来, 轻量级锁底层是通过自旋来实现的, 并不会阻塞线程, 如果自旋次数过多仍然没有获取到锁, 则会升级为重量级锁, 重量级锁会导致线程阻塞
3. 自旋锁: 自旋锁就是线程在获取锁的过程中, 不会去阻塞线程, 也就无所谓唤醒线程, 阻塞和唤醒这两个步骤都是需要操作系统去进行的, 比较消耗时间, 自旋锁是线程通过CAS获取预期的一个标记, 如果没有获取到, 则继续循环获取, 如果获取到了则表示获取到了锁, 这个过程线程一直在运行中, 相对而言没有使用太多的操作系统资源, 比较轻量

### 37.Synchronized和ReentrantLock的区别
1. synchronized是一个关键字, ReentrantLock是一个类
2. synchronized会自动的加锁和释放锁, ReentrantLock需要程序员手动加锁和释放锁
3. synchronized的底层是JVM层面的锁, ReentrantLock是API层面的锁
4. synchronized是非公平锁, ReentrantLock可以选择公平锁和非公平锁
5. synchronized锁的是对象, 锁信息保存在对象头中, ReentrantLock通过代码中int类型的state标识来标识锁的状态
6. synchronized底层有一个锁升级的过程

### 38.TCP的三次握手和四次挥手
TCP协议是7层网络协议中的传输层协议, 负责数据的可靠传输

在建立TCP连接时, 需要通过三次握手来建立, 过程是:
1. 客户端向服务端发送一个SYN
2. 服务端收到SYN后, 给客户端发送一个SYN_ACK
3. 客户端接收到SYN_ACK后, 再给服务端发送一个ACK

在断开TCP连接时, 需要通过四次挥手来断开, 过程是:
1. 客户端向服务端发送FIN
2. 服务端接收FIN后, 向客户端发送ACK, 表示我已收到断开连接的请求, 客户端你可以不发数据了, 不过服务端这边可能还有数据在处理
3. 服务端处理完所有数据后, 向客户端发送FIN, 表示服务端现在也可以断开连接了
4. 客户端收到服务端的FIN, 向服务端发送ACK, 表示客户端可会断开连接了

### 39.浏览器发出一个请求到收到响应经历了哪些步骤
1. 浏览器解析用户输入的URL, 生成一个Http格式的请求
2. 先根据URL域名从本地host文件查找是否有映射IP, 如果没有则将域名发送给电脑所配置的DNS进行解析, 得到IP地址
3. 浏览器通过操作系统将请求通过四层网络协议发送出去
4. 途中可能经过各种路由器、交换机, 最终到达服务器
5. 服务器收到请求后, 根据请求所指定的端口, 将请求传递传递给绑定了该端口的应用程序, 比如8080被tomcat占用了
6. tomcat接收到请求数据后, 按照Http协议的格式进行解析, 解析得到所到访问的servlet
7. servlet来处理该请求, 如果是SpringMVC则进入DispatcherServlet进而进入对应的Controller来处理请求, 并得到结果
8. Tomcat得到响应结果后封装成Http协议响应的格式, 并再次通过网络发送给浏览器所在的服务器
9. 浏览器所在的服务器拿到结果后再传递给浏览器, 浏览器则负责解析并渲染

### 40.ThreadLocal的底层原理
1. ThreadLocal是Java中所提供的线程本地存储机制, 可以利用该机制将数据缓存在某个线程内部, 则该线程可以在任意时刻、任意方法中获取缓存的数据
2. ThreadLocal底层是通过ThreadLocalMap来实现的, 每个Thread对象(注意不是ThreadLocal对象)中都存在一个ThreadLocalMap, Map的key为ThreadLocal对象, Map的value为需要缓存的值
3. 如果在线程池中使用ThreadLocal会造成内存泄露, 因为当ThreadLocal对象使用完之后, 应该要把设置的key, value, 也就是Entry对象进行回收, 
   但线程池中的线程不会回收, 而线程对象是通过强引用指向ThreadLocalMap, ThreadLocalMap也是通过强引用指向Entry对象, 线程不被回收, 
   Entry对象也不会被回收, 从而出现内存泄露, 解决方法是, 在使用了ThreadLocal对象后, 手动调用ThreadLocal的remove()方法, 手动清除Entry对象
4. ThreadLocal经典的应用场景就是连接管理(一个线程持有一个连接, 该连接对象可以在不同的方法之间进行传递, 线程之间不共享同一个连接)

### 41.并发, 并行, 串行之间的区别
1. 串行: 一个任务执行完, 才能执行下一个任务
2. 并行: 两个任务同时执行
3. 并发: 两个任务整体看上去是同时执行的, 在底层, 两个任务被拆成了很多份, 然后一个一个执行, 站在更高的角度看来两个任务是同时执行的

### 42.单例Bean和单例模式
单例模式表示JVM中某个类的对象只会存在唯一一个

而单例bean并不表示JVM中只能存在唯一一个某个类的bean对象

```java
/**
 * 例如: 
 *  1. 通过@Service可以生成一个 OrderService的bean也就是对象
 *  2. 也可以通过@Bean来生成一个 OrderService的bean也就是对象
 *  3. 也可以直接 OrderService orderService = new OrderService(); 获得一个OrderService的对象
 */
@Service // 生成的bean的name为 orderService
public class OrderService {
}

@Configuration // 生成的bean的name为 myConfig
public class MyConfig {
   @Bean
   public OrderService orderService2() { // 生成的bean的name为 orderService2
       return new OrderService();
   }
}
```

### 43.对守护线程的理解
线程分为用户线程和守护线程, 用户线程就是普通线程, 守护线程就是JVM的后台线程, 比如垃圾回收线程就是一个守护线程, 守护线程会在其他普通线程都停止运行之后,
自动关闭. 我们可以通过设置thread.setDaemon(true);来把一个线程设置为守护线程

### 44.分布式ID是什么, 有哪些解决方案
在开发中, 我们通常会需要一个唯一ID来标识数据, 如果是单体架构, 我们可以通过数据库的主键, 或直接在内存中维护一个自增数字来作为ID都是可以的, 但对于一个分布式系统, 就会有可能会出现ID冲突, 此时有以下解决方案:
1. uuid, 这种方案复杂度最低, 但是会影响存储空间和性能
2. 利用单机数据库的自增主键, 作为分布式ID的生成器, 复杂度适中, ID长度较之uuid更短, 但是受到单机数据库性能的限制, 并发量大的时候, 此方案也不是最优方案
3. 利用redis、zookeeper的特性来生成id, 比如redis的自增命令、zookeeper的顺序节点, 这种方案和单机数据库(mysql)相比, 性能有所提高, 可以适当选用
4. 雪花算法, 一切问题如果能直接用算法解决, 那就是最合适的, 利用雪花算法也可以生成分布式ID, 底层原理就是通过某台机器在某一毫秒内对某一个数字自增, 这种方案也能保证分布式架构中的系统id唯一, 但是只能保证趋势递增. 业界存在tinyid、leaf等开源中间件实现了雪花算法

### 45.分布式锁的使用场景是什么, 有哪些实现方案
在单体架构中, 多个线程都是属于同一个进程的, 所以在线程并发执行时, 遇到资源竞争时, 可以利用ReentrantLock、 synchronized等技术来作为锁, 来控制共享资源的使用

而在分布式架构中, 多个线程是可能处于不同进程中的, 而这些线程并发执行遇到资源竞争时, 利用ReentrantLock、 synchronized等技术是没办法来控制多个进程中的线程的, 所以需要分布式锁, 意思就是, 需要一个分布式锁生成器, 分布式系统中的应用程序都可以来使用这个生成器所提供的锁, 从而达到多个进程中的线程使用同一把锁

目前主流的分布式锁的实现方案有两种:
1. zookeeper: 利用的是zookeeper的临时节点、顺序节点、watch机制来实现的, zookeeper分布式锁的特点是高一致性, 因为zookeeper保证的是CP, 所以由它实现的分布式锁更可靠, 不会出现混乱
2. redis: 利用redis的setnx、lua脚本、消费订阅等机制来实现的, redis分布式锁的特点是高可用, 因为redis保证的是AP, 所以由它实现的分布式锁可能不可靠, 不稳定(一旦redis中的数据出现了不一致), 可能会出现多个客户端同时加到锁的情况
3. 1, 2中出现的 CP, AP 解释:
   * CAP理论是由 EricBrewer 教授 提出的, 在设计和部署分布式应用的时候, 存在三个核心的系统需求, 这个三个需求之间存在一定的特殊关系。三个需求如下:
   * C: Consistency(一致性)
   * A: Availability(可用性)
   * P: Partition Tolerance(分区容错性)
   * CAP理论的核心是: 一个分布式系统不可能同时很好的满足 一致性, 可用性和分区容错性这三个需求, 最多只能同时较好的满足两个
   * 因此, 根据CAP原理, 将NoSQL数据库分成满足CA原则、CP原则和AP原则 三大类:
   * CA - 单点集群, 满足一致性, 可用性的系统, 通常在可扩展性上不太强大. (传统数据库)
   * CP - 满足一致性, 分区容忍性的系统, 通常性能不是特别高. (Redis、MongoDB) (对Redis保证的是 AP还是CP 意见有点不一致, `https://blog.csdn.net/weixin_43475992/article/details/136280242`)
   * AP - 满足可用性, 分区容忍性的系统, 通常可能对一致性要求低一些. (大多数网站架构的选择)
   
### 46.负载均衡算法有哪些
1. 轮询法: 将请求按顺序轮流地分配到后端服务器上, 它均衡地对待后端的每一台服务器, 而不关心服务器实际的连接数和当前的系统负载
2. 随机法: 通过系统的随机算法, 根据后端服务器的列表大小值来随机选取其中的一台服务器进行访问. 由概率统计理论可以得知, 随着客户端调甲服务端的次数增多, 其实际效果越来越接近于平均分配调用量到后端的每一台服务器, 也就是轮询的结果
3. 源地址哈希法: 源地址哈希的思想是根据获取客户端的IP地址, 通过哈希函数计算得到的一个数值, 用该数值对服务器列表的大小进行取模运算, 得到的结果便是客户端要访问服务器的序号. 采用源地址哈希法进行负载均衡, 同一IP地址的客户端, 当后端服务器列表不变时, 它每次都会映射到同一台后端服务器进行访问
4. 加权轮询法: 不同的后端服务器可能机器的配置和当前系统的负或并不相同, 因此它们的抗压能力也不相同. 给配置高负载低的机器配置更高的权重, 让其处理更多的请求; 而配置低、负载高的机器, 给其分配较低的权重, 降低其系统负载, 加权轮询能很好地处理这一问题, 并将请求顺序且按照权重分配到后端
5. 加权随机法: 与加权轮询法一样, 加权随机法也根据后端机器的配置, 系统的负载分配不同的权重。 不同的是, 它是按照权重随机请求后端服务器, 而非顺序
6. 最小连接数法: 最小连接数算法比较灵活和智能, 由于后端服务器的配置不尽相同, 对于请求的处理有快有慢, 它是根据后端服务器当前的连接情况, 动态地选取其中当前积压连接数最少的一台服务器来处理当前的请求, 尽可能地提高后端服务的利用效率, 将负责合理地分流到每一台服务器。

### 47.缓存穿透, 缓存击穿, 缓存雪崩分别是什么
缓存中存放的大多都是热点数据, 目的就是防止请求可以直接从缓存中获取到数据, 而不用访问Mysql.
1. 缓存雪崩: 如果缓存中某一时刻大批热点数据同时过期, 那么就可能导致大量请求直接访问Mysql了, 解决办法就是在过期时间上增加一点随机值, 另外如果搭建一个高可用的Redis集群也是防止缓存雪崩的有效手段
2. 缓存击穿∶ 和缓存雪崩类似, 缓存雪崩是大批热点数据失效, 而缓存击穿是指某一个热点key突然失效, 也导致了大量请求直接访问Mysql数据库, 这就是缓存击穿, 解决方案就是考虑这个热点key不设过期时间
3. 缓存穿透: 假如某一时刻访问redis的大量key都在redis中不存在(比如黑客故意伪造一些乱七八糟的key), 那么也会给数据造成压力, 这就是缓存穿透, 解决方案是使用布隆过滤器, 它的作用就是如果它认为一个key不存在, 那么这个key就肯定不存在, 所以可以在缓存之前加一层布隆过滤器来拦截不存在的key

### 48.简述MyISAM和InnoDB的区别
MyISAM:
* 不支持事务, 但是每次查询都是原子的
* 支持表级锁, 即每次操作是对整个表加锁
* 存储表的总行数
* 一个MyISAM表有三个文件: 索引文件, 表结构文件, 数据文件 (分别为: .myi, .frm, .myd)
* 采用非聚集索引, 索引文件的数据域存储指向数据文件的指针. 辅索引与主索引基本一致, 但是辅索引不用保证唯一性

InnoDB:
* 支持ACID的事务, 支持事务的四种隔离级别
* 支持行级锁和外键约束: 因此可以支持写并发
* 不存储总行数
* 一个InnoDb引擎存储在一个文件空间(共享表空间, 表大小不受操作系统控制, 一个表可能分布在多个文件里), 也有可能为多个(设置为独立表空, 表大小受操作系统文件大小限制, 一般为2G), 受操作系统文件大小的限制;
* 主键索引采用聚集索引(索引的数据域存储数据文件本身), 辅索引的数据域存储主键的值; 因此从辅索引查找数据, 需要先通过辅索引找到主键值, 再访问辅索引; 最好使用自增主键, 防止插入数掘时为维持B+树结构, 文件的大调整

### 49.零拷贝是什么
零拷贝指的是, 应用程序在需要把内核中的一块区域数据转移到另外一块内核区域去时, 不需要经过先复制到用户空间, 再转移到目标内核区域去了, 而直接实现转移

### 50.