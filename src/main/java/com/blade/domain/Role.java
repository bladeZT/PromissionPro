package com.blade.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter@ToString
public class Role {
    private Long rid;

    private String rnum;

    private String rname;
    //一个角色可以有多个权限
    List<Permission> permissions = new ArrayList<>();

}