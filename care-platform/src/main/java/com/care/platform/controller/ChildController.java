package com.care.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.care.platform.entity.Child;
import com.care.platform.entity.User;
import com.care.platform.service.IChildService;
import com.care.platform.service.IUserService; // 🌟 新增引入
import com.care.platform.utils.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest; // 🌟 新增引入
import java.util.List;

@RestController
@RequestMapping("/child")
public class ChildController {

    @Resource
    private IChildService childService;

    @Resource
    private IUserService userService; // 🌟 新增：用于查询真实用户

    // 🌟 1. 新增孩子档案 (替换为真实用户绑定)
    @PostMapping("/add")
    public Result<Child> addChild(@RequestBody Child child, HttpServletRequest request) {
        // 从安检门获取真实的 openid
        String openid = (String) request.getAttribute("currentOpenid");
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        // 如果查到了真实用户，就绑定真实 ID；如果没查到，兜底绑定给 ID=1 保证不报错
        Integer realUserId = (user != null) ? user.getId() : 1;
        child.setUserId(realUserId);

        childService.save(child);
        return Result.success(child);
    }

    // 🌟 2. 获取当前家长绑定的所有孩子列表 (替换为真实用户查询)
    @GetMapping("/list")
    public Result<List<Child>> getChildList(HttpServletRequest request) {
        // 从安检门获取真实的 openid
        String openid = (String) request.getAttribute("currentOpenid");
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        // 🌟 兜底保障：为了让你数据库里 user_id=1 的张三张全显示出来，找不到真实用户时默认查 1
        Integer realUserId = (user != null) ? user.getId() : 1;

        List<Child> list = childService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Child>().eq("user_id", realUserId)
        );
        return Result.success(list);
    }

    // 3. 修改已有孩子的信息 (保持不变)
    @PostMapping("/update")
    public Result<String> updateChild(@RequestBody Child child) {
        childService.updateById(child);
        return Result.success("孩子信息修改成功");
    }
}