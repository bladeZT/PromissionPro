package com.blade.aspect;

import com.blade.domain.Systemlog;
import com.blade.mapper.SystemlogMapper;
import com.blade.util.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aopalliance.intercept.Joinpoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SystemAspect {

    @Autowired
    private SystemlogMapper systemlogMapper;

    //JoinPoint 获取开启切面的那个方法的连接点
    public void writeLog(JoinPoint joinPoint) throws JsonProcessingException {
        System.out.println("记录日志");
        //设置日志内容
        Systemlog systemlog = new Systemlog();

        //设置时间
        systemlog.setOptime1(new Date());
        //设置ip地址,这个在request对象中，这里拿不到，要添加一个拦截器，获取请求对象
        //通过本地线程取得request对象
        HttpServletRequest request = RequestUtil.getRequest();
        if (request != null){
            //如果是网页地址是localhost的形式，那么ip是0:0:0:0:0:0:0:1
            //如果是http://127.0.0.1:8080/index.jsp，那么ip是127.0.0.1
            String ip = request.getRemoteAddr();
            System.out.println("ip = " + ip);
            systemlog.setIp1(ip);
        }
        //设置方法
        //获取目标执行方法的全路径
        String name = joinPoint.getTarget().getClass().getName();
        //获取方法名称
        String signature = joinPoint.getSignature().getName();
        String func = name + ":" + signature;
        systemlog.setFunction1(func);
        System.out.println("func = " + func);
        //获取方法参数,并将其转换成Json格式
        String params = new ObjectMapper().writeValueAsString(joinPoint.getArgs());
        System.out.println("params = " + params);
        systemlog.setParams1(params);
        System.out.println("systemlog = " + systemlog);

        systemlogMapper.insert(systemlog);


    }
}
