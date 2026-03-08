package com.care.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.care.platform.entity.Timeslot;
// 注意：如果有红线，检查是不是没带 I，代码生成器默认是 ITimeslotService
import com.care.platform.service.ITimeslotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 时间段表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/timeslot")
public class TimeslotController {

    @Resource
    private ITimeslotService timeslotService;

    /**
     * 查询时间段列表
     * GET /timeslot/list?start_date=2026-03-05&end_date=2026-03-12
     */
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam("start_date") String startDate,
                                    @RequestParam("end_date") String endDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 🌟 核心过滤逻辑：使用 LambdaQueryWrapper 划定日期边界
            LambdaQueryWrapper<Timeslot> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.ge(Timeslot::getDate, startDate)   // ge = Greater or Equal (大于等于起始日期)
                    .le(Timeslot::getDate, endDate)     // le = Less or Equal (小于等于结束日期)
                    .orderByAsc(Timeslot::getDate)      // 第一排序：按日期从小到大排
                    .orderByAsc(Timeslot::getStartTime);// 第二排序：同一天内按开始时间从小到大排

            // 去数据库捞数据
            List<Timeslot> list = timeslotService.list(queryWrapper);

            // 组装成咱们 API 文档里规定的标准 JSON 格式返回
            result.put("status", "success");
            result.put("data", list);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "获取时间段失败：" + e.getMessage());
        }

        return result;
    }
}