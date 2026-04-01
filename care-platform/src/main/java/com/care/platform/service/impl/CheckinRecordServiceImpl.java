package com.care.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.care.platform.entity.CheckinRecord;
import com.care.platform.entity.Reservation;
import com.care.platform.mapper.CheckinRecordMapper;
import com.care.platform.service.ICheckinRecordService;
import com.care.platform.service.IReservationService;
import com.care.platform.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import com.care.platform.utils.GeoUtil;

@Service
public class CheckinRecordServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements ICheckinRecordService {

    @Resource
    private IReservationService reservationService;
    // 假设你已经注入了 CenterLocationService (获取托管中心坐标)
    // @Resource private ICenterLocationService centerLocationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> processCheckin(Map<String, Object> params) {
        // 1. 获取前端传来的经纬度和照片
        Double userLat = Double.valueOf(params.get("latitude").toString());
        Double userLng = Double.valueOf(params.get("longitude").toString());
        String faceImageUrl = (String) params.get("face_image"); // 人脸图片

        // 2. 地理围栏安全校验 (Geofencing)
        // 这里假设托管中心的坐标是写死的，实际应从 center_location 表查询
        double centerLat = 36.651216;
        double centerLng = 117.120000;
        int maxRadius = 300; // 允许的误差半径 300米

        double distance = GeoUtil.getDistance(userLat, userLng, centerLat, centerLng);
        if (distance > maxRadius) {
            return Result.error("签到失败：您当前距离托管中心 " + Math.round(distance) + " 米，不在签到范围内！");
        }

        // 3. 人脸识别校验 (预留对接腾讯云)
        /* TODO: 调用腾讯云 Face API
           Float similarity = TencentCloudFaceApi.compare(childFaceBase, faceImageUrl);
           if (similarity < 80.0f) { return Result.error("人脸比对失败，请确保是孩子本人！"); }
        */

        // ... 下面的逻辑和原先一样：写入 CheckinRecord 表、修改 Reservation 状态
        // record.setImageUrl(faceImageUrl);
        // ...

        return Result.success("签到成功！距离中心 " + Math.round(distance) + " 米");
    }
}