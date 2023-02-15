package com.wuwei.redisdemo;

import com.wuwei.redisdemo.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wuwei.redisdemo.mapper")
public class RedisdemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(RedisdemoApplication.class, args);
        System.out.println("git add 后进行");
        System.out.println("git 提交");
        System.out.println("git =====提交");

    }

}
