package com.example.demo.controller;

import com.example.demo.service.impl.demo1impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class demo1Controller {

    @Autowired
    private demo1impl server;
    /**
     * 接收数据并保存到数据库中
     * @param data
     */
    @ResponseBody
    @PostMapping("/submit")
    public void submit(@RequestBody List<String> data){
        System.out.println("接收到data::"+ data);
        server.addList(data);


    }


}
