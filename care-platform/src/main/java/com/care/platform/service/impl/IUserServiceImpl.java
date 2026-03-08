package com.care.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.care.platform.entity.User;
import com.care.platform.mapper.UserMapper;
import com.care.platform.service.IUserService;
import com.care.platform.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 🌟 已填入你提供的真实微信凭证
    private String appId = "wx1853f84992db233d";
    private String appSecret = "0019cb8a90a8d2da5241227f8e63ff26";

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String loginByWechat(String code) {
        // 构建请求微信官方接口的 URL
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
                "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";

        // 发送请求获取 openid
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(response);
        String openid = jsonObject.getString("openid");

        // 如果获取不到 openid，抛出异常
        if (openid == null) {
            throw new RuntimeException("微信登录失败：" + response);
        }

        // 检查数据库是否已有该用户
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        // 如果是新用户，则自动注册存入数据库
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setIsMember(false); // 默认非会员
            user.setCreatedAt(LocalDateTime.now()); // 记录注册时间
            this.save(user);
        }

        // 签发 JWT Token 返回给前端
        return JwtUtils.generateToken(openid);
    }
}