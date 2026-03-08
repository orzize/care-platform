package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 * 时间段表
 * </p>
 */
@Data
@TableName("timeslot")
public class Timeslot {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 最大预约人数
     */
    private Integer maxCount;

    /**
     * 当前已预约人数
     */
    private Integer currentCount;
}