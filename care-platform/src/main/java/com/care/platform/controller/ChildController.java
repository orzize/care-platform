package com.care.platform.controller;

import com.care.platform.entity.Child;
import com.care.platform.service.IChildService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/child")
public class ChildController {

    @Resource
    private IChildService childService;

    @PostMapping("/add")
    public Map<String, Object> addChild(@RequestBody Child child) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 🌟 核心修复：给数据库一个交代，暂时默认绑定给 ID=1 的家长
            // 等后续全面联调后端鉴权时，这里再换成 Token 解析出来的真实 userId
            child.setUserId(1);

            // 将前端传来的真实数据存入 child 表中
            childService.save(child);

            result.put("status", "success");
            result.put("message", "孩子档案创建成功");
            result.put("data", child); // 将带有真实 ID 的数据返回给前端
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "保存失败：" + e.getMessage());
        }
        return result;
    }

    // ... 你原有的 addChild 方法保留不动 ...

    // 🌟 1. 新增：获取当前家长绑定的所有孩子列表
    @GetMapping("/list")
    public Map<String, Object> getChildList() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 严格遵循之前的逻辑：暂时默认查询 user_id = 1 的家长（后续联调Token时替换）
            List<Child> list = childService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Child>().eq("user_id", 1)
            );
            result.put("status", "success");
            result.put("data", list);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "获取列表失败：" + e.getMessage());
        }
        return result;
    }

    // 🌟 2. 新增：修改已有孩子的信息
    @PostMapping("/update")
    public Map<String, Object> updateChild(@RequestBody Child child) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 必须传真实 id 过来，MyBatis-Plus 会根据 id 自动更新其余字段
            childService.updateById(child);
            result.put("status", "success");
            result.put("message", "孩子信息修改成功");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "修改失败：" + e.getMessage());
        }
        return result;
    }
}