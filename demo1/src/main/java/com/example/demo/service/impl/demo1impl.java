package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Demo1;
import com.example.demo.mapper.demo1mapper;
import com.example.demo.service.demo1Service;
import com.example.demo.utils.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class demo1impl implements demo1Service {
    @Autowired
    private demo1mapper mapper;

    public Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addList(List<String> list){

        try{
            for (String item : list) {
                QueryWrapper<Demo1> wrapper = new QueryWrapper<>();
                /*
                 * 数据格式：2023-09-13 20:53:13  LV:14  Speed:056  num:00000397
                 * 分成     2023-09-13 20:53:13, LV:14, Speed:056, num:00000397
                 *    是空格占位符，因为前端传来的，只能用这个分开
                 */
                MyUtil util = new MyUtil();
                List<String> result = util.splitData(item,"  ");       //以若干个空格分开
                Demo1 demo1 = new Demo1();
                // 转换时间
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                demo1.setCreatetime(ft.parse(result.get(0)));

                /*
                 * 数据格式： LV:14, Speed:056, num:00000397
                 * 转化为：[LV, 29][Speed, 041][num, 00000107
                 * ]--num的最后有一个\n需要删除
                 */
                List<String> ls = util.splitData(result.get(1),":");
                demo1.setLv(Integer.parseInt(ls.get(1)));
                ls = util.splitData(result.get(2),":");
                demo1.setSpeed(Integer.parseInt(ls.get(1)));
                ls = util.splitData(result.get(3),":");
                ls = util.splitData(ls.get(1),"\n");
                demo1.setNum(Integer.parseInt(ls.get(0)));

                /*   拿完数据，开始与数据库中的数据比较，看是否已经存在
                 */
                wrapper.eq("createtime",new java.sql.Timestamp(demo1.getCreatetime().getTime()));
                Demo1 demo11=mapper.selectOne(wrapper);
                if(demo11 != null){
                    System.out.println("存在："+demo11);

                }else {
                    System.out.println("添加成功！"+demo1);
                    mapper.insert(demo1);

                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public List<Demo1> getAll(){
        QueryWrapper<Demo1> wrapper = new QueryWrapper<>();
        return mapper.selectList(wrapper.select());
    }

}
