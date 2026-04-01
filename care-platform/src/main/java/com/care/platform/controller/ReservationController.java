package com.care.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.care.platform.entity.Child;
import com.care.platform.entity.Reservation;
import com.care.platform.entity.Timeslot;
import com.care.platform.entity.User;
import com.care.platform.service.IChildService;
import com.care.platform.service.IReservationService;
import com.care.platform.service.ITimeslotService;
import com.care.platform.service.IUserService;
import com.care.platform.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Resource
    private IReservationService reservationService;
    @Resource
    private IUserService userService;
    @Resource
    private ITimeslotService timeslotService;
    @Resource
    private IChildService childService;

    // 🌟 1. 获取最新预约 (已瘦身，调用 Service)
    @GetMapping("/latest")
    public Result<Map<String, Object>> getLatestReservation(HttpServletRequest request) {
        String openid = (String) request.getAttribute("currentOpenid");
        return reservationService.getLatestReservationInfo(openid);
    }

    // 🌟 2. 创建预约 (已瘦身，调用 Service 且受事务保护)
    @PostMapping("/create")
    public Result<Map<String, Integer>> create(@RequestBody Map<String, Integer> requestParams, HttpServletRequest request) {
        String openid = (String) request.getAttribute("currentOpenid");
        return reservationService.executeBooking(requestParams, openid);
    }

    // 🌟 3. 找回失踪的历史列表接口！(保留原有的完整逻辑)
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getHistoryList(HttpServletRequest request) {
        String openid = (String) request.getAttribute("currentOpenid");
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) return Result.error("未找到用户");

        // 查询该家长的所有预约记录
        List<Reservation> list = reservationService.list(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getUserId, user.getId())
                        .orderByDesc(Reservation::getId)
        );

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Reservation r : list) {
            Timeslot timeslot = timeslotService.getById(r.getTimeslotId());
            Child child = childService.getById(r.getChildId());

            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("childName", child != null ? child.getName() : "未知");
            map.put("date", timeslot != null ? timeslot.getDate() : "未知");

            // 安全截取时间字符串
            String timeStr = timeslot != null ? timeslot.getStartTime().toString().substring(0, 5) + "-" + timeslot.getEndTime().toString().substring(0, 5) : "未知";
            map.put("time", timeStr);

            map.put("status", r.getStatus());
            map.put("createTime", r.getCreatedAt());
            resultList.add(map);
        }
        return Result.success(resultList);
    }
}