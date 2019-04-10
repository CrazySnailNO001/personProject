package com.xzh.personalproject.selfTaught.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XZHH
 * @Description: 阻塞式线程安全队列, 基于数组实现
 * @create 2019/3/8 0008 14:35
 * @modify By:
 **/
public class BlockingQueue {
    List<String> list = new ArrayList<>();
    private int maxSize;

    //自定义一把锁
    private Object lock = new Object();

    private ReentrantLock reentrantLock;

    public BlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        System.out.println(Thread.currentThread().getName() + "初始化完成,大小为" + this.maxSize);
        System.out.println("================================================");
    }

    public void put(String element) {
        synchronized (lock) {
            if (this.list.size() == this.maxSize) {
                System.out.println(Thread.currentThread().getName() + "队列已经满了,put方法进入等待状态...");
                try {
                    lock.wait();    //释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //存元素
            this.list.add(element);
            System.out.println(Thread.currentThread().getName() + "向队列中添加了元素: " + element);
            System.out.println("================================================");

            //唤醒其他线程
            lock.notifyAll();
        }
    }

    public void take() {
        final ReentrantLock reentrantLock = (ReentrantLock) this.lock;
        synchronized (lock) {
            if (this.list.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "队列已经空了,take方法进入到等待状态...");
                try {
                    lock.wait();    //释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String result = list.get(0);
            list.remove(0);
            System.out.println(Thread.currentThread().getName() + "取到了元素: " + result);

            //唤醒其他线程
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        BlockingQueue blockingQueue = new BlockingQueue(5);

        new Thread(() -> {
            blockingQueue.put("1");
            blockingQueue.put("2");
            blockingQueue.put("3");
            blockingQueue.put("4");
            blockingQueue.put("5");
            blockingQueue.put("6");
            blockingQueue.put("7");
        }, "T1-put").start();

        new Thread(() -> {
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
            blockingQueue.take();
        }, "T2-take").start();
    }
}
