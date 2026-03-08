package com.care.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信openId
     */
    private String openid;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 是否会员
     */
    private Boolean isMember;

    /**
     * 会员开始时间
     */
    private LocalDateTime memberStart;

    /**
     * 会员结束时间
     */
    private LocalDateTime memberEnd;

    /**
     * 用户创建时间
     */
    private LocalDateTime createdAt;
}