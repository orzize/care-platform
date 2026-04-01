package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("checkin_record")
public class CheckinRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer childId;
    private Integer timeslotId;
    private String imageUrl; // 预留给二期拍照
    private String result;
    private BigDecimal similarity; // 预留给三期人脸识别
    private LocalDateTime createTime;
}