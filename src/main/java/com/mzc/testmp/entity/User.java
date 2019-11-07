package com.mzc.testmp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * create by zhencai.ma on 2019/10/22
 */
@TableName(value = "tb_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
}
