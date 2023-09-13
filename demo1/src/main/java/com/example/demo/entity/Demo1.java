package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Demo1 {
    @TableId(type = IdType.AUTO)
    public int lv;
    public int speed;
    public Date createtime;
    public  double num;
    public int id;


}
