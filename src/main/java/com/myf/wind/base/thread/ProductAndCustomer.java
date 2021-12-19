package com.myf.wind.base.thread;

import java.util.LinkedList;

/**
 * 题目一：
 * 用多线程实现一个生产者和消费者模式。一个线程负责往List里put 1-100的数字，另外一个线程负责get数字并进行累加，
 * 要求：
 * 1、交替打印put的数据和累加的结果。
 * 2、功能完成、可运行。
 * @author wind_myf
 */
public class ProductAndCustomer {

    private static volatile Integer n = 0;
    private volatile Integer j = 0;
    private static volatile Integer sum = 0;
    private static final LinkedList<Integer> list = new LinkedList<>();

    public void product() throws InterruptedException {
        synchronized (list) {
            while (list.size() <= 0) {
                System.out.println("生产中");
                list.wait();
            }
            list.push(n);
            n++;
            System.out.println("生产者生产了一个" + n);
            list.notifyAll();
        }
    }

    public void customer() throws InterruptedException {
        synchronized (list) {
            while (list.size() == 0) {
                System.out.println("等待生产者生产");
                list.wait();
            }
            list.notifyAll();
            j++;
            sum = sum + list.pop();
            System.out.println("当前" + j + "个数相加的和为：" + sum);
        }
    }

    public static void main(String[] args) {
        ProductAndCustomer productAndCustomer = new ProductAndCustomer();
        int time = 100;
        new Thread(() -> {
            try {
                for (int i = 0; i <= time; i++) {
                    productAndCustomer.product();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i <= time; i++) {
                    productAndCustomer.customer();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}