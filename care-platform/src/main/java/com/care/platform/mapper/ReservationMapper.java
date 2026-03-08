package com.care.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.care.platform.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {

    // 自定义连表查询：把 reservation 表和 timeslot 表关联起来
    @Select("SELECT r.id as reservationId, r.status, r.create_time as createTime, " +
            "t.date_str as dateStr, t.start_time as startTime, t.end_time as endTime " +
            "FROM reservation r " +
            "LEFT JOIN timeslot t ON r.timeslot_id = t.id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.create_time DESC")
    List<Map<String, Object>> getUserReservations(@Param("userId") Long userId);
}