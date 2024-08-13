package org.pjj;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

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
