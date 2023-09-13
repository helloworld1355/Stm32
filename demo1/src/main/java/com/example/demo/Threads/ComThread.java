package com.example.demo.Threads;

import com.example.demo.controller.WebSocketServer;
import com.example.demo.utils.ComUtil;

/**
 *创建一个端口发送数据的线程
 *这个线程会卡住监听串口，需要添加一个子线程来发送数据给前端
 */
public class ComThread implements Runnable{

    private final WebSocketServer server;
    private final String comport;

    public ComThread(WebSocketServer server,String comport){
        this.server=server;
        this.comport=comport;
    }

    @Override
    public void run(){
        System.out.println("进入父线程");
        ComUtil test=new ComUtil(server);
        test.SingleComport(comport);
    }
}
