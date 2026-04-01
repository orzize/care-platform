package com.care.platform.interceptor;

import com.care.platform.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局 Token 鉴权拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行 OPTIONS 请求 (这是浏览器发起的跨域预检请求，直接放行)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 从请求头中获取 Token
        String tokenHeader = request.getHeader("Authorization");

        // 3. 拦截：如果没有携带 Token，或者格式不对
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            // 直接抛出异常，咱们的 GlobalExceptionHandler 会接管它，并返回优雅的 JSON 报错！
            throw new RuntimeException("非法请求：无权访问，请先登录！");
        }

        // 4. 提取真实的 Token 并解析
        String token = tokenHeader.substring(7);
        String openid = JwtUtils.getOpenidFromToken(token);

        // 5. 拦截：Token 过期或被篡改
        if (openid == null) {
            throw new RuntimeException("登录状态已过期，请重新登录！");
        }

        // 🌟 6. 终极架构技巧：将解析出来的 openid 挂载到 request 请求对象上！
        // 这样后面放行进入 Controller 后，Controller 直接从 request 里拿，再也不用自己解 Token 了！
        request.setAttribute("currentOpenid", openid);

        // 7. 验证全部通过，尊贵放行！
        return true;
    }
}