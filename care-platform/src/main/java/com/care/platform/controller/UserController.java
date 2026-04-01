package com.care.platform.controller;

import com.care.platform.service.IUserService;
import com.care.platform.utils.Result; // 🌟 引入全局统一返回类
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        // 🌟 核心业务调用：传入 code，换取真实的 token
        // 如果这里因为 code 过期或无效抛出异常，会被全局拦截器直接捕获，返回给前端优雅的错误提示
        String token = userService.loginByWechat(code);

        // 极简返回：将 token 包装成 Map 塞入 Result 成功状态中返回
        return Result.success(Collections.singletonMap("token", token));
    }
}