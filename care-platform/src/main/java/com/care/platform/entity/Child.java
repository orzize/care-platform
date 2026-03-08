package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("child")
public class Child {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId; // 关联的家长ID
    private String name;
    private Integer age;
    private String grade;
    private String parentName;  // 对应新增的 parent_name
    private String parentPhone; // 对应新增的 parent_phone
    private String note;        // 🌟 完美适配你数据库原有的 note 字段
}