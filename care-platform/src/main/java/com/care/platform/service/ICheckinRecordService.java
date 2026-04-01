package com.care.platform.service;

import com.care.platform.utils.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ICheckinRecordService {
    @Transactional(rollbackFor = Exception.class)
    Result<String> processCheckin(Map<String, Object> params);
}
