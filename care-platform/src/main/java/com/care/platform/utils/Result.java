package com.care.platform.utils;

// 这是一个泛型类，用于统一后端返回给前端的数据格式
public class Result<T> {
    private String status;  // 状态："success" 或 "error"
    private String message; // 提示信息
    private T data;         // 承载的真实数据

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus("success");
        result.setData(data);
        return result;
    }

    // 成功（仅提示信息）
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setStatus("success");
        result.setMessage(message);
        return result;
    }

    // 失败（报错信息）
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setStatus("error");
        result.setMessage(message);
        return result;
    }

    // Getter 和 Setter (必须有，否则 Spring 无法转换为 JSON)
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}