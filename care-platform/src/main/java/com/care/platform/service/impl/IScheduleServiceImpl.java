package com.care.platform.service.impl;

import com.care.platform.entity.Schedule;
import com.care.platform.mapper.ScheduleMapper;
import com.care.platform.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 排班表 服务实现类
 * </p>
 *
 * @author Developer
 * @since 2026-03-04
 */
@Service
public class IScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

}
