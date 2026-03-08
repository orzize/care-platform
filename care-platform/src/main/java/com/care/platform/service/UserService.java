package com.care.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.care.platform.entity.User;

public interface UserService extends IService<User> {
    /**
     * 微信小程序登录
     */
    String loginByWechat(String code);
}