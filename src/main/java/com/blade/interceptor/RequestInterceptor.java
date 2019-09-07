package com.blade.interceptor;

import com.blade.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("来到了拦截器");
        //将request存到本地线程中，作为全局静态变量
        RequestUtil.setRequest(request);
//        System.out.println("request = " + request);
        return true;
    }
}
