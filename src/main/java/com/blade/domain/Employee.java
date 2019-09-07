package com.blade.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter@Getter@ToString
public class Employee {
    private Long id;

    private String username;
    private String password;
    //加这个从数据库取出来的时候，这个属性在转成json，时候会把值，转成对应的格式，后面那个代表我们是在东八区, timezone = "GMT+8"，可是加了这个反而不对了，于是去掉了
    @JsonFormat(pattern = "yyyy-MM-dd")
    //提交表单的时候，是按下面这个格式提交的，会自动去转换
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inputtime;

    private String tel;

    private String email;

    private Boolean state;

    private Boolean admin;

    private Department department;

    private List<Role> roles = new ArrayList<>();

}