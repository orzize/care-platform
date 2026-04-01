package com.care.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.care.platform.entity.User;
import com.care.platform.service.IUserService;
import com.care.platform.utils.JwtUtils;
import com.care.platform.utils.Result; // 🌟 引入全局返回类
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private IUserService userService;

    /**
     * 1. 模拟：创建支付订单
     * 前端带上 Token 和套餐 ID 来请求
     */
    @PostMapping("/createOrder")
    public Result<Map<String, String>> createOrder(@RequestHeader("Authorization") String tokenHeader, @RequestBody Map<String, Integer> request) {
        // 校验身份
        String token = tokenHeader.replace("Bearer ", "");
        String openid = JwtUtils.getOpenidFromToken(token);
        if (openid == null) {
            return Result.error("登录已过期，请重新登录");
        }

        // 模拟生成一个 10 位的随机订单号
        String orderId = "MOCK_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        Map<String, String> data = new HashMap<>();
        data.put("order_id", orderId);
        data.put("pay_params", "模拟支付参数"); // 真实环境下这里是返回给微信拉起弹窗的参数

        return Result.success(data);
    }

    /**
     * 2. 模拟：支付成功回调（发放会员权益）
     * 真实环境下这个接口是由微信服务器调用的，现在我们让前端模拟调用
     */
    @PostMapping("/notify")
    public Result<String> payNotify(@RequestHeader("Authorization") String tokenHeader, @RequestBody Map<String, String> request) {
        // 校验身份
        String token = tokenHeader.replace("Bearer ", "");
        String openid = JwtUtils.getOpenidFromToken(token);

        // 查出是谁在买单
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        if (user == null) {
            return Result.error("找不到该用户");
        }

        // 🌟 核心业务闭环：充值成功，发放 30 天会员！
        user.setIsMember(true);
        user.setMemberStart(LocalDateTime.now());
        user.setMemberEnd(LocalDateTime.now().plusDays(30)); // 默认加 30 天

        userService.updateById(user); // 更新数据库

        return Result.success("支付成功！已为您开通 30 天会员！");
    }
}