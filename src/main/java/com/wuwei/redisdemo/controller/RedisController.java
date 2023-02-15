package com.wuwei.redisdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuwei.redisdemo.mapper.UserMapper;
import com.wuwei.redisdemo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisController {

   @Autowired
   private StringRedisTemplate stringRedisTemplate;     //操作字符串

   @Autowired
   private RedisTemplate<Object,Object> redisTemplate;  //  操作对象的

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;
    @RequestMapping("index")
    @ResponseBody
    public String index(){
        String value = stringRedisTemplate.opsForValue().get("aaa");
        return value;
    }

    @RequestMapping("setKeyObject")
    @ResponseBody
    public String index1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",111);
        User lcs = userMapper.selectOne(queryWrapper);
        System.out.println(lcs);
        redisTemplate.opsForValue().set("lcs",lcs);
        return "setKeyObject success";
    }
    @RequestMapping("setKeyObjectJson")
    @ResponseBody
    public String index2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",111);
        User lcs = userMapper.selectOne(queryWrapper);
        System.out.println(lcs);
        userRedisTemplate.opsForValue().set("lcs",lcs);
        return "setKeyObjectJson success";
    }
}
