package com.xzh.personalproject.selfTaught.thread;

/**
 * @author XZHH
 * @Description:
 * @create 2019/3/8 0008 13:53
 * @modify By:
 **/
public class WaitNOtifyDemo {
    /*
    * notify wait 必须结合synchronized同步锁使用
    * */
    public synchronized void run1() {
        System.out.println("进入run1方法");
        try {
            //this是当前对象,synchronized使用的是this当前对象的锁
            //释放锁,当前线程后面的代码就不会被执行了
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
        }
        System.out.println("run1执行完成");
    }

    public synchronized void run2() {
        System.out.println("进入run2方法");
        try {
            //释放锁,当前线程后面的代码就不会被执行了
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("run2执行完成");
    }

    public synchronized void run3() {
        System.out.println("进入run3方法");
        this.notifyAll();
//        this.notify();      //唤醒等待池中其中一个现场执行,但是不释放锁(将下面的代码执行完了,才会去执行其他被唤醒线程中后面的代码)
        System.out.println("run3执行完成,通知其他等待的线程执行");
    }

    public static void main(String[] args) {
        final WaitNOtifyDemo demo = new WaitNOtifyDemo();
        new Thread(() -> {
            demo.run1();
        }, "T1").start();

        new Thread(() -> {
            demo.run2();
        }, "T2").start();

        new Thread(() -> {
            demo.run3();
        }, "T3").start();
    }
}
