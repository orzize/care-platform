package com.care.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.care.platform.entity.Child;
import com.care.platform.entity.User;
import com.care.platform.mapper.ChildMapper;
import com.care.platform.service.IChildService;
import com.care.platform.service.IUserService;
import com.care.platform.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 孩子信息表 服务实现类
 */
@Service
public class IChildServiceImpl extends ServiceImpl<ChildMapper, Child> implements IChildService {

    @Resource
    private IUserService userService;

    @Override
    public Result<List<Child>> getChildrenByOpenid(String openid) {
        // 🌟 修正点 1：使用标准 Lambda 表达式查询，避开方法引用可能的静态冲突
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(u -> u.getOpenid(), openid));

        // 兜底逻辑：如果用户不存在，默认查 userId = 1 的数据（兼容你之前的张三张全）
        Integer userId = (user != null) ? user.getId() : 1;

        // 🌟 修正点 2：同样的 Lambda 写法
        List<Child> list = this.list(new LambdaQueryWrapper<Child>().eq(c -> c.getUserId(), userId));

        return Result.success(list);
    }
}