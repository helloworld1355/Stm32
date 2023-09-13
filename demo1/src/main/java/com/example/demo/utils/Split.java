package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Split {
    /**
     * 将 字符串data以re分隔符分割
     * @param data
     * @param Re
     * @return  已re分割的字符串数组
     */
    public List<String> splitData(String data,String Re) {
        System.out.println("进入splitData");
        String regex = Re;  // 以一个或多个空格为分隔符
        Pattern pattern = Pattern.compile(regex);
        String[] parts = pattern.split(data);
        System.out.println("parts:"+Arrays.toString(parts));
        return new ArrayList<>(Arrays.asList(parts));
    }

}
