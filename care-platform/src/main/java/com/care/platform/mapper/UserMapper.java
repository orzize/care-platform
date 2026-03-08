package com.care.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.care.platform.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表的 Mapper 接口
 * 继承 BaseMapper 之后，所有基础的 CRUD（增删改查）就自动搞定了！
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}