package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MyUtil {
    /**
     * 将 字符串data以re分隔符分割
     * @param data
     * @param Re
     * @return  已re分割的字符串数组
     */
    public List<String> splitData(String data,String Re) {
        String regex = Re;  // 以一个或多个空格为分隔符
        Pattern pattern = Pattern.compile(regex);
        String[] parts = pattern.split(data);
        return new ArrayList<>(Arrays.asList(parts));
    }

    public String DateToDatetime(Date date){
        // 创建一个 SimpleDateFormat 实例，指定所需的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        return sdf.format(date);
    }

    public Date DatetimeToDate(String date){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=new Date();
        try{
            date1=ft.parse(date);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return date1;
    }

}
