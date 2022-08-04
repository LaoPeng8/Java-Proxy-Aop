package org.pjj.gof23;

import org.pjj.gof23.singleton.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 测试单例模式
 * @author PengJiaJun
 * @Date 2022/08/04 12:15
 */
public class Main {
    public static void main(String[] args) {
        //饿汉式
        Demo01 demo01 = Demo01.getInstance();
        Demo01 demo011 = Demo01.getInstance();
        System.out.println(demo01 == demo011);//true

        //懒汉式
        Demo02 demo02 = Demo02.getInstance();
        Demo02 demo022 = Demo02.getInstance();
        System.out.println(demo02 == demo022);//true

        //静态内部类式
        Demo04 demo04 = Demo04.getInstance();
        Demo04 demo044 = Demo04.getInstance();
        System.out.println(demo04 == demo044);//true

        //枚举方式
        Demo05 demo05 = Demo05.INSTANCE;
        Demo05 demo055 = Demo05.INSTANCE;
        System.out.println(demo05 == demo055);//true

        //以懒汉式为例子 使用反射 破解单例
        try {
            Class<?> clz = Class.forName("org.pjj.gof23.singleton.Demo01");
            Constructor<?> constructor = clz.getDeclaredConstructor();//获取单例类的私有构造器
            constructor.setAccessible(true);
            Demo01 o1 = (Demo01) constructor.newInstance();//通过单例类的私有构造器, new出对象
            Demo01 o2 = (Demo01) constructor.newInstance();//通过单例类的私有构造器, new出对象
            System.out.println(o1 == o2);//false 破解了单例

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //Demo06的单例, 防止了 反射调用的漏洞 (只能防止饿汉式, 不能防止懒汉式)
        try {
            Class<?> clz = Class.forName("org.pjj.gof23.singleton.Demo06");
            Constructor<?> constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Demo06 o1 = (Demo06) constructor.newInstance();
            Demo06 o2 = (Demo06) constructor.newInstance();
            System.out.println(o1 == o2);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //通过反序列化的方式构造多个对象
        try {
            Demo06 o1 = Demo06.getInstance();//通过单例提供的方法获取单例对象 (正常的获取单例对象)
            FileOutputStream fileOutputStream = new FileOutputStream("E:/a.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o1);//序列化 (将对象写到硬盘)
            objectOutputStream.close();
            fileOutputStream.close();


            //反序列化 (将对象从硬盘读取到内存)
            FileInputStream fileInputStream = new FileInputStream("E:/a.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Demo06 o2 = (Demo06) objectInputStream.readObject();
            System.out.println(o1 == o2);//false (将正常得到的单例对象 与 反序列化后的单例对象比较 发现是两个不同的对象)
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //防止 通过反序列化的方式构造多个对象
        //在单例对象的类上加入方法, 再次通过反序列化时 发现 (正常得到的单例对象 与 反序列化后的单例对象 是两个一样的对象)
//        public Object readResolve() throws ObjectStreamException {
//            return instance;
//        }

    }
}
