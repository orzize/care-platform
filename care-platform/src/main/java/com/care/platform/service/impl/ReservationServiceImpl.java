package com.care.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.care.platform.entity.Reservation;
import com.care.platform.entity.Timeslot;
import com.care.platform.entity.User;
import com.care.platform.mapper.ReservationMapper;
import com.care.platform.service.IReservationService;
import com.care.platform.service.ITimeslotService;
import com.care.platform.service.IUserService;
import com.care.platform.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 开启事务

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {

    @Resource
    private IUserService userService;
    @Resource
    private ITimeslotService timeslotService;

    @Override
    public Result<Map<String, Object>> getLatestReservationInfo(String openid) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) return Result.success(null);

        Reservation latest = this.getOne(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getUserId, user.getId())
                        .orderByDesc(Reservation::getId).last("limit 1")
        );

        if (latest != null) {
            Timeslot timeslot = timeslotService.getById(latest.getTimeslotId());
            Map<String, Object> task = new HashMap<>();
            task.put("date", timeslot.getDate());
            task.put("time", timeslot.getStartTime().toString().substring(0, 5) + "-" + timeslot.getEndTime().toString().substring(0, 5));
            task.put("status", "pending");
            task.put("statusText", "🕒 待送达到班");
            task.put("service", "系统已记录您的预约");
            return Result.success(task);
        }
        return Result.success(null);
    }

    // 🌟 核心：加 @Transactional 保证发生异常时，预约和库存操作一起回滚！
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Map<String, Integer>> executeBooking(Map<String, Integer> requestParams, String openid) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) return Result.error("用户不存在");
        if (user.getIsMember() == null || !user.getIsMember() || user.getMemberEnd().isBefore(LocalDateTime.now())) {
            return Result.error("您还不是会员或会员已过期，请先开通会员！");
        }

        Timeslot timeslot = timeslotService.getById(requestParams.get("timeslot_id"));
        if (timeslot == null) return Result.error("该时间段不存在");
        if (timeslot.getCurrentCount() >= timeslot.getMaxCount()) return Result.error("手慢了，该时间段名额已满！");

        Reservation reservation = new Reservation();
        reservation.setUserId(user.getId());
        reservation.setChildId(requestParams.get("child_id"));
        reservation.setTimeslotId(timeslot.getId());
        reservation.setStatus("待审核");
        reservation.setCreatedAt(LocalDateTime.now());
        this.save(reservation);

        timeslot.setCurrentCount(timeslot.getCurrentCount() + 1);
        timeslotService.updateById(timeslot);

        Map<String, Integer> data = new HashMap<>();
        data.put("reservation_id", reservation.getId());
        return Result.success(data);
    }
}