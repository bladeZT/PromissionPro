package com.blade.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    //通过本地线程，创建全局静态变量
    public static ThreadLocal<HttpServletRequest> local = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return local.get();
    }

    public static void setRequest(HttpServletRequest request) {
        local.set(request);
    }
}
