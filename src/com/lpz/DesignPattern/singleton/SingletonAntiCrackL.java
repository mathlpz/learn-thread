package com.lpz.DesignPattern.singleton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

/**
 * 单例模式的五种实现方式中，除枚举式是天然的单例不可破解之外，其他四种形式均可通过反射和反序列化的机制进行破解。
 * <p>
 * 以懒汉式单例为例，首先分别看一下如何通过”反射“和“反序列化”的机制破解单例。
 *
 * @Author: lpz
 * @Date: 2019-04-25 09:20
 */
public class SingletonAntiCrackL {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Singleton2 instance1 = Singleton2.getInstance();
        Singleton2 instance2 = Singleton2.getInstance();
        System.out.println(instance1);
        System.out.println(instance2);

        // 利用反射机制破解单例，创建多个不同的实例：
        try {
            Class<Singleton2> clazz = (Class<Singleton2>)Class.forName("com.lpz.DesignPattern.singleton.Singleton2");
            Constructor<Singleton2> constructor = clazz.getDeclaredConstructor(null);
            constructor.setAccessible(true); // //跳过权限检查，可以访问私有属性和方法
            Singleton2 singleton2 = clazz.newInstance();
            Singleton2 singleton3 = clazz.newInstance();
            System.out.println(singleton2);
            System.out.println(singleton3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // JDK8运行异常java.lang.IllegalAccessException，private构造方法，获取不到单实例... 不知为啥
        // 修改单例的构造函数，可以应对反射机制的破解，代码如下：（私有构造器，增加实例检查；若已创建实例，则抛出异常）


        //通过反序列化机制破解单例（对象类必须可序列化，继承Serializable接口）
//        try {
//            //序列化，将对象存入文件
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("d:/singleton.txt"));
//            oos.writeObject(instance1);
//            oos.close();
//            //反序列化，从文件中读出对象
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("d:/singleton.txt"));
//            Singleton2 sing = (Singleton2)ois.readObject();
//            System.out.println(sing);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // 解决办法：单例类中重写readResolve方法，可以应对反射机制的破解，代码如下：


    }


}
