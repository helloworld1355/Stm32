package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Demo1;
import com.example.demo.mapper.demo1mapper;
import com.example.demo.service.demo1Service;
import com.example.demo.utils.Split;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class demo1impl implements demo1Service {
    @Autowired
    private demo1mapper mapper;

    @Override
    public Boolean addList(List<String> list){
        QueryWrapper<Demo1> wrapper = new QueryWrapper<>();
        try{
            for (String item :
                    list) {
                /**
                 * 数据格式：2023-09-13 20:53:13  LV:14  Speed:056  num:00000397
                 * 分成     2023-09-13 20:53:13, LV:14, Speed:056, num:00000397
                 *    是空格占位符，因为前端传来的，只能用这个分开
                 */
                Split split = new Split();
                List<String> result = split.splitData(item,"  ");       //以若干个空格分开
                Demo1 demo1 = new Demo1();
                // 转换时间
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                demo1.setCreatetime(ft.parse(result.get(0)));

                /**
                 * 数据格式： LV:14, Speed:056, num:00000397
                 * 转化为：
                 */
                List<String> ls = split.splitData(result.get(1),":");
                System.out.println(ls);
                demo1.setLv(Integer.parseInt(ls.get(1)));
                ls = split.splitData(result.get(2),":");
                demo1.setSpeed(Integer.parseInt(ls.get(1)));
                ls = split.splitData(result.get(3),":");
                ls = split.splitData(ls.get(1),"\n");
                demo1.setNum(Integer.parseInt(ls.get(0)));

                System.out.println("demo1:"+demo1);
                System.out.println(mapper.selectList(wrapper.select()));
                mapper.insert(demo1);

//                待修改
                wrapper.eq("createtime",result);
                System.out.println(wrapper.getSqlSelect());
                System.out.println(mapper.selectList(wrapper));;

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

}
