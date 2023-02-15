package com.wuwei.redisdemo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "xxx_user")
@Data
public class User implements Serializable {

    private Integer userId;
    private String userName;
    private String password;
}
