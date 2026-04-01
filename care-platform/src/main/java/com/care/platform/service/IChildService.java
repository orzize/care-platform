package com.care.platform.service;

import com.care.platform.entity.Child;
import com.baomidou.mybatisplus.extension.service.IService;
import com.care.platform.utils.Result;

import java.util.List;

/**
 * <p>
 * 孩子信息表 服务类
 * </p>
 *
 * @author Developer
 * @since 2026-03-04
 */
public interface IChildService extends IService<Child> {
    // 增加一个根据 openid 获取孩子列表的方法
    Result<List<Child>> getChildrenByOpenid(String openid);
}
