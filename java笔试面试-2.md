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
### 26
### 27