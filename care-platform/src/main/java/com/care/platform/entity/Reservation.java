package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {

    // 全部改成 Integer！
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer childId;

    private Integer timeslotId;

    private String status;

    // 旧的叫 createTime，现在改成 createdAt
    private LocalDateTime createdAt;
}