package com.care.platform.controller;

import com.care.platform.entity.Timeslot;
import com.care.platform.entity.Volunteer;
import com.care.platform.service.ITimeslotService;
import com.care.platform.service.IVolunteerService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private ITimeslotService timeslotService;

    @Resource
    private IVolunteerService volunteerService;

    // 🌟 1. 修改时段人数上限
    @PostMapping("/timeslot/updateCapacity")
    public Map<String, Object> updateCapacity(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer id = params.get("id");
            Integer maxCount = params.get("maxCount");

            Timeslot timeslot = timeslotService.getById(id);
            if (timeslot != null) {
                timeslot.setMaxCount(maxCount);
                timeslotService.updateById(timeslot);
                result.put("status", "success");
            } else {
                result.put("status", "error");
                result.put("message", "找不到该时段");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 🌟 2. 获取志愿者龙虎榜单 (按总工时倒序)
    @GetMapping("/volunteer/list")
    public Map<String, Object> getVolunteerList() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 真实从你的 volunteer 表里查数据，按工时最高排前面！
            List<Volunteer> list = volunteerService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Volunteer>().orderByDesc("total_hours")
            );
            result.put("status", "success");
            result.put("data", list);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 🌟 3. 对志愿者进行排班并增加工时
    @PostMapping("/schedule/assign")
    public Map<String, Object> assignVolunteer(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer scheduleId = params.get("scheduleId"); // 待分配时段的ID
            Integer volunteerId = params.get("volunteerId"); // 你在手机上选中的志愿者ID

            Volunteer v = volunteerService.getById(volunteerId);
            if (v != null) {
                // 核心业务：每次指派成功，给该志愿者增加 3 个服务工时！
                v.setTotalHours(v.getTotalHours() + 3.0);
                volunteerService.updateById(v);

                result.put("status", "success");
                result.put("message", "指派成功！已记录工时");
            } else {
                result.put("status", "error");
                result.put("message", "找不到志愿者");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 🌟 4. 新增：基于公平算法的一键自动分配志愿者
    @PostMapping("/schedule/autoAssign")
    public Map<String, Object> autoAssignVolunteer(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer scheduleId = params.get("scheduleId"); // 待分配的时段 ID

            // 🚀 排班算法核心：贪心策略找“最闲”的人
            // 在所有可用志愿者中，按总工时(total_hours)升序排列，取第一名！
            List<Volunteer> availableVols = volunteerService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Volunteer>()
                            .eq("status", "1") // 过滤出当前可接单的志愿者 (如果你的表里是 '1' 就写 "1")
                            .orderByAsc("total_hours") // 升序排列：工时最少的排在最前面
                            .last("limit 1") // 只取第一名
            );

            if (availableVols != null && !availableVols.isEmpty()) {
                Volunteer bestMatch = availableVols.get(0);

                // 自动分配并增加 3.0 工时
                bestMatch.setTotalHours(bestMatch.getTotalHours() + 3.0);
                volunteerService.updateById(bestMatch);

                // 实际项目中这里还会更新 schedule 表的记录状态...

                result.put("status", "success");
                result.put("message", "算法分配成功！已指派给：" + bestMatch.getName());
                result.put("data", bestMatch); // 把分给了谁告诉前端
            } else {
                result.put("status", "error");
                result.put("message", "当前没有处于可用状态的志愿者！");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "自动排班异常：" + e.getMessage());
        }
        return result;
    }
    // 🌟 5. 新增：管理员手动添加新的排班时段 (带防重复校验)
    @PostMapping("/timeslot/add")
    public Map<String, Object> addTimeslot(@RequestBody Timeslot timeslot) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 🚀 核心修复：去数据库做查重校验
            // 查找是否已经存在：同日期 + 同开始时间 + 同结束时间 的排班
            long existCount = timeslotService.count(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Timeslot>()
                            .eq("date", timeslot.getDate())
                            .eq("start_time", timeslot.getStartTime())
                            .eq("end_time", timeslot.getEndTime())
            );

            // 如果数量大于 0，说明这个时间段已经被添加过了，直接无情拦截！
            if (existCount > 0) {
                result.put("status", "error");
                result.put("message", "该排班时段已存在，请勿重复添加！");
                return result; // 提前结束，绝对不执行 save
            }

            // 校验通过，允许放行。新创建的排班，默认已预约人数肯定是 0
            timeslot.setCurrentCount(0);
            timeslotService.save(timeslot);

            result.put("status", "success");
            result.put("message", "排班新增成功");

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "新增失败：" + e.getMessage());
        }
        return result;
    }
    // 🌟 6. 新增：管理员删除指定的排班时段
    @PostMapping("/timeslot/delete")
    public Map<String, Object> deleteTimeslot(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer id = params.get("id");
            // 利用 MyBatis-Plus 直接根据 ID 从数据库物理删除
            timeslotService.removeById(id);

            result.put("status", "success");
            result.put("message", "排班已删除");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
}