package com.care.platform.controller;

import com.care.platform.service.ICheckinRecordService;
import com.care.platform.utils.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    @Resource
    private ICheckinRecordService checkinRecordService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Map<String, Object> params) {
        return checkinRecordService.processCheckin(params);
    }
}