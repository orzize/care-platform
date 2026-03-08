package com.care.platform.service.impl;

import com.care.platform.entity.Reservation;
import com.care.platform.mapper.ReservationMapper;
import com.care.platform.service.IReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预约表 服务实现类
 * </p>
 *
 * @author Developer
 * @since 2026-03-04
 */
@Service
public class IReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {

}
