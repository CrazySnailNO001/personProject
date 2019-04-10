package com.xzh.personalproject.selfTaught;

/**
 * @author XZHH
 * @Description: 输出结果:     Myparent static blok  hello world
 * 顺序是这样的 1.父类创建实例 2.父类静态常量初始化null 3.父类静态代码块 4.子类创建实例 5.子类静态常量初始化 6.子类静态代码块 7.子类静态代码块赋值
 * @create 2019/3/19 0019 17:18
 * @modify By:
 **/
public class MyTest {
    public static void main(String[] args) {
        System.out.println(MyChild.str);
    }
}

class Myparent {
    public static String str = "hello world";

    static {
        System.out.println("Myparent static blok");
    }
}

class MyChild extends Myparent {
    public static String str2 = "Welcom";

    static {
        System.out.println("MyChild static blok");
    }
}