package com.care.platform.exception; // 注意改成你实际的包名

import com.care.platform.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器
 * 只要 Controller 里发生任何报错，都会被这里自动拦截，并包装成漂亮的 JSON 返回给前端
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 打印错误日志到控制台，方便咱们后台排查
        e.printStackTrace();

        // 给前端返回极其干净的错误 JSON
        return Result.error("服务器开小差了：" + e.getMessage());
    }
}