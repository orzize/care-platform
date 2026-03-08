package com.care.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.care.platform.entity.User;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {
    // 微信登录核心逻辑声明
    String loginByWechat(String code);
}