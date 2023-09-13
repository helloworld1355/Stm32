package com.example.demo.Threads;

import com.example.demo.controller.WebSocketServer;
import com.example.demo.utils.ComUtil;

/**
 *创建一个端口发送数据的线程
 *这个线程会卡住监听串口，需要添加一个子线程来发送数据给前端
 */
public class ComThread implements Runnable{

    private final WebSocketServer server;

    public ComThread(WebSocketServer server){
        this.server=server;
    }

    @Override
    public void run(){
        System.out.println("进入父线程");
        ComUtil test=new ComUtil(server);
        //所有串口
        test.AllComport();
    }
}
