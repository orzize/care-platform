package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 志愿者排班表
 * </p>
 */
@Data
@TableName("schedule")
public class Schedule {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 🌟 统一为 Integer

    /**
     * 时间段ID
     */
    private Integer timeslotId;

    /**
     * 志愿者ID
     */
    private Integer volunteerId;

    /**
     * 排班状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}