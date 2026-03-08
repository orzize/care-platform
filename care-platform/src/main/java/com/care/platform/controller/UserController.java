package com.care.platform.controller;

import com.care.platform.service.IUserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String code = request.get("code");
            String token = userService.loginByWechat(code); // 调用真实微信接口

            result.put("status", "success");
            result.put("data", Collections.singletonMap("token", token));
        } catch (Exception e) {
            // 🌟 拦截异常，把真正的死因（如 invalid code）返回给前端！不再 undefined！
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}