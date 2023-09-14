package com.example.demo.controller;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{id}")
public class WebSocketServer {

    public Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * 客户端ID
     */
    private String id = "";

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 记录当前在线连接数(为保证线程安全，须对使用此变量的方法加lock或synchronized)
     */
    private static int onlineCount = 0;

    /**
     * 用来存储当前在线的客户端(此map线程安全)
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功后调用
     */
    @OnOpen
    public void onOpen(@PathParam(value = "id") String id, Session session) {
        this.session = session;
        // 接收到发送消息的客户端编号
        this.id = id;
        // 加入map中
        webSocketMap.put(id, this);
        // 在线数加1
        addOnlineCount();
        log.info("客户端" + id + "加入，当前在线数为：" + getOnlineCount());
        try {
            sendMessage("WebSocket连接成功");
        } catch (IOException e) {
            log.error("WebSocket IO异常");
        }
    }

    /**
     * 推送信息给指定ID客户端，如客户端不在线，则返回不在线信息给自己
     *
     * @param message 客户端发来的消息
     * @param sendClientId 客户端ID
     */
    public void sendToUser(String message, String sendClientId) throws IOException {
        if (webSocketMap.get(sendClientId) != null) {
            if (!id.equals(sendClientId)) {
                webSocketMap.get(sendClientId)
                        .sendMessage("客户端" + id + "发来消息：" + " <br/> " + message);
            } else {
                webSocketMap.get(sendClientId).sendMessage(message);
            }
        } else {
            // 如客户端不在线，则返回不在线信息给自己
            sendToUser("当前客户端不在线", id);
        }
    }


    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose() {
        // 从map中删除
        webSocketMap.remove(this.id);
        // 在线数减1
        subOnlineCount();
        log.info("有一连接关闭，当前在线数为：" + getOnlineCount());
    }


    /**
     * 发生错误时回调
     *
     * @param session 用户信息
     * @param error 错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误");
        System.out.println("出错");
        error.printStackTrace();
    }


    
    public static void sendToWeb(String message, String sendClientId) throws IOException {
        if (webSocketMap.get(sendClientId) != null) {
           webSocketMap.get(sendClientId).sendMessage(message);
        } else {
            // 如客户端不在线，则返回不在线信息给自己
            System.out.println(sendClientId + "=当前客户端不在线");
        }
    }



    /**
     * 发送消息
     * @param message 要发送的消息
     */
    private void sendMessage(String message) throws IOException {
        //System.out.println("发送");
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 收到客户端消息后调用
     * @param message 客户端发送过来的消息<br/>
     *                消息格式：内容 - 表示群发，内容|X - 表示发给id为X的客户端
     * @param session 用户信息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        String[] messages = message.split("[|]");
        System.out.println("message:"+ Arrays.toString(messages));
        try {
            if (messages.length > 1) {
                sendToUser(messages[0], messages[1]);
            } else {
                sendToAll(messages[0]);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 群送发送信息给所有人
     *
     * @param message 要发送的消息
     */
    public void sendToAll(String message) throws IOException {
        //System.out.println("群发");
        for (String key : webSocketMap.keySet()) {
            webSocketMap.get(key).sendMessage(message);
        }
    }

    /**
     * 获取在线人数
     * @return 在线人数
     */
    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 有人上线时在线人数加一
     */
    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 有人下线时在线人数减一
     */
    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}