package com.example.demo.utils;

import com.example.demo.Threads.ComThread_son;
import com.example.demo.controller.WebSocketServer;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * @Classname ComUtil
 * @Description com串口通信
 * @Date 2023/9/12
 * @Created by qiusuyang
 */
public class ComUtil {

    private String data;
    private final WebSocketServer server;
    public ComUtil(WebSocketServer server){
        this.server=server;
    }
    /**
     * <com名称,SerialPort>串口通信map，存储串口名称与串口信息
     */
  //  private Map<String, SerialPort> comMap = new HashMap<>();
    /**
     * com口列表
     */
   // private List<String> comList = new ArrayList<>();

    //监听单一 comport
    public String SingleComport(String comName){
        System.out.println("进入串口");
        SerialPort serialPort=SerialPort.getCommPort(comName);
        serialPort.openPort();
        System.out.println("进入串口接收");
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                //将数据传出去
                data=new String(serialPortEvent.getReceivedData());
                ComThread_son thread_son=new ComThread_son(data,server);
                thread_son.run();

            }
        });
        System.out.println("data:"+data);
        return data;
    }

    //    public void AllComport() {
//        //将所有的串口信息放入comlist,comMap中
//        SerialPort[] commPorts = SerialPort.getCommPorts();
//        for (SerialPort commPort : commPorts) {
//            comList.add(commPort.getSystemPortName());
//            comMap.put(commPort.getSystemPortName(), commPort);
//            //监听所有串口通信的数据
//            commPort.openPort();
//            commPort.addDataListener(new SerialPortDataListener() {
//                @Override
//                public int getListeningEvents() {
//                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
//                }
//
//                @Override
//                public void serialEvent(SerialPortEvent serialPortEvent) {
//                    byte[] newData = serialPortEvent.getReceivedData();
//                    //将数据传出去
//                    ComPort com=new ComPort();
//                    com.GetMassage(new String(newData));
////                    System.err.println(String.format("串口%s接收到数据大小：%s,串口数据内容:%s"
////                            ,serialPortEvent.getSerialPort().getSystemPortName(),newData.length,new String(newData)));
//                }
//            });
//        }
//    }

}