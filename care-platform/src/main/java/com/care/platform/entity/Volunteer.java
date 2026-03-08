package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("volunteer")
public class Volunteer {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
    private String skillTags;     // 映射 skill_tags
    private String availableTime; // 映射 available_time
    private String status;        // 映射 status

    private String avatar;        // 映射 avatar
    private String phone;         // 映射 phone
    private Double totalHours;    // 映射 total_hours
}