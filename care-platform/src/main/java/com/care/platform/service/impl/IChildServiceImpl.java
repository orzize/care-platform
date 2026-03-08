package com.care.platform.service.impl;

import com.care.platform.entity.Child;
import com.care.platform.mapper.ChildMapper;
import com.care.platform.service.IChildService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 孩子信息表 服务实现类
 * </p>
 *
 * @author Developer
 * @since 2026-03-04
 */
@Service
public class IChildServiceImpl extends ServiceImpl<ChildMapper, Child> implements IChildService {

}
