package com.xzh.personalproject.selfTaught.thread.model;

/**
 * @author XZHH
 * @Description:
 * @create 2019/3/8 0008 11:51
 * @modify By:
 **/
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println(Thread.currentThread().getName() + "==>" + this.toString());
    }

    public void changeUser(String name, int age) {
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.age = age;
        System.out.println(Thread.currentThread().getName() + "==>" + this.toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
