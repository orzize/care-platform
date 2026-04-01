package com.care.platform.service;

import com.care.platform.entity.Reservation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.care.platform.utils.Result;

import java.util.Map;

/**
 * <p>
 * 预约表 服务类
 * </p>
 *
 * @author Developer
 * @since 2026-03-04
 */
public interface IReservationService extends IService<Reservation> {
    // 业务下沉 1：获取最新预约
    Result<Map<String, Object>> getLatestReservationInfo(String openid);
    // 业务下沉 2：执行真正的预约核心逻辑
    Result<Map<String, Integer>> executeBooking(Map<String, Integer> requestParams, String openid);
}
