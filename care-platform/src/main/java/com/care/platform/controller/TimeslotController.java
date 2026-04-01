package com.care.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.care.platform.entity.Timeslot;
import com.care.platform.service.ITimeslotService;
import com.care.platform.utils.Result; // 🌟 引入全局返回类
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/timeslot")
public class TimeslotController {

    @Resource
    private ITimeslotService timeslotService;

    /**
     * 查询时间段列表
     * GET /timeslot/list?start_date=xxx&end_date=xxx
     */
    @GetMapping("/list")
    public Result<List<Timeslot>> list(@RequestParam("start_date") String startDate,
                                       @RequestParam("end_date") String endDate) {

        // 🌟 核心过滤逻辑：使用 LambdaQueryWrapper 划定日期边界
        LambdaQueryWrapper<Timeslot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Timeslot::getDate, startDate)   // ge = Greater or Equal
                .le(Timeslot::getDate, endDate)         // le = Less or Equal
                .orderByAsc(Timeslot::getDate)          // 第一排序：按日期从小到大排
                .orderByAsc(Timeslot::getStartTime);    // 第二排序：同一天内按开始时间从小到大排

        // 去数据库捞数据，直接塞给 Result 返回！
        List<Timeslot> list = timeslotService.list(queryWrapper);
        return Result.success(list);
    }
}