package com.myf.wind.base.nio;


import com.myf.wind.base.nio.SelectorThreadGroup;

/**
 * @author : wind-myf
 * @date : 2021/12/17 5:45
 * @desc : 主线程
 * @version : 1.0
 */
public class MainTest {
    public static void main(String[] args) {



        // 创建IO线程（一个或多个）
        SelectorThreadGroup group = new SelectorThreadGroup(3);
        // 工作
        SelectorThreadGroup workGroup = new SelectorThreadGroup(3);



        // 注册监听的server
        group.bind(9999);
        group.setWorkerGroup(workGroup);

    }
}
