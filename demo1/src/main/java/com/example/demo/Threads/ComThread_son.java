package com.example.demo.Threads;

import com.example.demo.controller.WebSocketServer;

import java.io.IOException;

/**
 * ComThread线程的子线程，因为ComThread进入了串口监听就不能结束，所以在串口监听写这个线程，
 * 这个线程作用是发送串口数据
 */
public class ComThread_son implements Runnable{
    private final String data;
    private final WebSocketServer server;

    public ComThread_son(String data,WebSocketServer server){
        this.data=data;
        this.server=server;
    }
    @Override
    public void run(){
        try {
            server.sendToAll(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
