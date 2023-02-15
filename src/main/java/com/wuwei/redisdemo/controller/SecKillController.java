package com.wuwei.redisdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Controller
public class SecKillController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @RequestMapping("seckillpage")
    public String indexPage(){
        return "seckillPage";
    }
    @RequestMapping("doseckill")
    @ResponseBody
    public  String doseck(String productid){

        String userid="user"+new Random().nextInt(10000);
        return String.valueOf(seckill(userid,productid));
    }

    private  boolean seckill(String userid,String productid){
        //1. 验证非空处理  空 retur false
        if (!StringUtils.hasLength(userid)||!StringUtils.hasLength(productid)){
            System.out.println("空值处理");
            return false;
        }
        // 2 拼接resis里面的key
        String kcKey="sku:"+productid+":kc";
        String usersKey="sku:"+productid+":user";
        // 3 秒杀  秒杀过程  秒杀没开始 秒杀已经结束   重复秒杀

        String kc = stringRedisTemplate.opsForValue().get(kcKey);
        if (kc==null){
            System.err.println("秒杀活动还没开始");
            return false;
        }

        if (Integer.parseInt(kc)<=0){
            System.err.println("秒杀已经结束");
            return false;
        }

        if (stringRedisTemplate.opsForSet().isMember(usersKey,userid)){
            System.err.println("重复秒杀");
            return false;
        }


        stringRedisTemplate.setEnableTransactionSupport(true);
//        stringRedisTemplate.watch(kcKey);
//        stringRedisTemplate.multi();
//        stringRedisTemplate.opsForValue().decrement(kcKey);
//        stringRedisTemplate.opsForSet().add(usersKey,userid);
//        stringRedisTemplate.exec();



        SessionCallback callback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch(kcKey);
                String o = (String) redisOperations.opsForValue().get(kcKey);
                int i = Integer.parseInt(o);
                if (i-1<0){
                    return null;
                }

                redisOperations.multi();
                redisOperations.opsForValue().decrement(kcKey);
                redisOperations.opsForSet().add(usersKey,userid);


                return  redisOperations.exec();
            }
        };
        List<Object> resultList = (List<Object>) stringRedisTemplate.execute(callback);
        if (resultList==null||resultList.size()==0){
            System.err.println("秒杀失败");
            return false;
        }
        System.out.println("秒杀成功");
        return true;
    }
}

