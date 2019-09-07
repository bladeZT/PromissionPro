package com.blade.web.filter;

import com.blade.domain.AjaxRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyFormFilter extends FormAuthenticationFilter {

    /*监听到认证成功*/
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {

        System.out.println("认证成功");
        AjaxRes ajaxRes = new AjaxRes();
        ajaxRes.setMsg("登录成功");
        ajaxRes.setSuccess(true);
        //把对象转成json格式的字符串
        String jsonString = new ObjectMapper().writeValueAsString(ajaxRes);
        System.out.println("jsonString = " + jsonString);
        response.setCharacterEncoding("utf-8");
        //这种方法响应给浏览器，只能响应字符串
        response.getWriter().print(jsonString);

        return false;
    }

    /*监听到认证失败*/
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        System.out.println("认证失败");

        AjaxRes ajaxRes = new AjaxRes();

        ajaxRes.setSuccess(false);

        if(e != null){
            //获取异常的名称
            String name = e.getClass().getName();
            if(name.equals(UnknownAccountException.class.getName())){
                //没有账号
                ajaxRes.setMsg("账号不正确");
            }else if(name.equals(IncorrectCredentialsException.class.getName())){
                //密码错误
                ajaxRes.setMsg("密码不正确");
            }else{
                //未知异常
                ajaxRes.setMsg("未知错误");
                e.printStackTrace();
            }

        }

        try {
            //把对象转成json格式的字符串
            String jsonString = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(jsonString);
        } catch (IOException ex) {


        }

        return false;
    }
}
