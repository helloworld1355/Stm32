package com.example.demo.controller;

import com.example.demo.Threads.ComThread;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class webController {
    @Autowired
    private WebSocketServer server;

    @GetMapping("/")
    public String test(){
        System.out.println("进入");
        ComThread thread=new ComThread(server);
        thread.run();
        return "test";
    }



//    @ResponseBody
//    @GetMapping("/getallCom")
//    public List<String> getall(){
//        SerialPort[] commPorts = SerialPort.getCommPorts();
//        List<String> list = new ArrayList<>();
//        for (SerialPort commPort : commPorts){
//            list.add(commPort.getSystemPortName());
//        }
//        System.out.println(list);
//        return list;
//    }

}
