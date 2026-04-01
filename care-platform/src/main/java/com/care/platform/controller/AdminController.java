package com.care.platform.controller;

import com.care.platform.entity.Timeslot;
import com.care.platform.entity.Volunteer;
import com.care.platform.service.ITimeslotService;
import com.care.platform.service.IVolunteerService;
import com.care.platform.utils.Result; // 🌟 引入咱们的全局统一返回类
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private ITimeslotService timeslotService;

    @Resource
    private IVolunteerService volunteerService;

    // 🌟 1. 修改时段人数上限 (瘦身版：从 20 行缩减到 6 行)
    @PostMapping("/timeslot/updateCapacity")
    public Result<String> updateCapacity(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("id");
        Integer maxCount = params.get("maxCount");

        Timeslot timeslot = timeslotService.getById(id);
        if (timeslot == null) return Result.error("找不到该时段");

        timeslot.setMaxCount(maxCount);
        timeslotService.updateById(timeslot);
        return Result.success("修改成功");
    }

    // 🌟 2. 获取志愿者龙虎榜单 (瘦身版：从 15 行缩减到 4 行)
    @GetMapping("/volunteer/list")
    public Result<List<Volunteer>> getVolunteerList() {
        List<Volunteer> list = volunteerService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Volunteer>().orderByDesc("total_hours")
        );
        return Result.success(list);
    }

    // 🌟 3. 对志愿者进行排班并增加工时
    @PostMapping("/schedule/assign")
    public Result<String> assignVolunteer(@RequestBody Map<String, Integer> params) {
        Integer volunteerId = params.get("volunteerId");

        Volunteer v = volunteerService.getById(volunteerId);
        if (v == null) return Result.error("找不到志愿者");

        v.setTotalHours(v.getTotalHours() + 3.0);
        volunteerService.updateById(v);
        return Result.success("指派成功！已记录工时");
    }

    // 🌟 4. 一键自动分配志愿者 (贪心算法不变，代码结构极简)
    @PostMapping("/schedule/autoAssign")
    public Result<Volunteer> autoAssignVolunteer(@RequestBody Map<String, Integer> params) {
        List<Volunteer> availableVols = volunteerService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Volunteer>()
                        .eq("status", "1")
                        .orderByAsc("total_hours")
                        .last("limit 1")
        );

        if (availableVols == null || availableVols.isEmpty()) {
            return Result.error("当前没有处于可用状态的志愿者！");
        }

        Volunteer bestMatch = availableVols.get(0);
        bestMatch.setTotalHours(bestMatch.getTotalHours() + 3.0);
        volunteerService.updateById(bestMatch);

        return Result.success("算法分配成功！已指派给：" + bestMatch.getName());
    }

    // 🌟 5. 管理员手动添加新的排班时段 (防重复校验拦截不变)
    @PostMapping("/timeslot/add")
    public Result<String> addTimeslot(@RequestBody Timeslot timeslot) {
        long existCount = timeslotService.count(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Timeslot>()
                        .eq("date", timeslot.getDate())
                        .eq("start_time", timeslot.getStartTime())
                        .eq("end_time", timeslot.getEndTime())
        );

        if (existCount > 0) return Result.error("该排班时段已存在，请勿重复添加！");

        timeslot.setCurrentCount(0);
        timeslotService.save(timeslot);
        return Result.success("排班新增成功");
    }

    // 🌟 6. 管理员删除指定的排班时段
    @PostMapping("/timeslot/delete")
    public Result<String> deleteTimeslot(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("id");
        timeslotService.removeById(id);
        return Result.success("排班已删除");
    }
}