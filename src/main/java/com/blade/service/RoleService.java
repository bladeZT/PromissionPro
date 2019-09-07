package com.blade.service;

import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import com.blade.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RoleService {
    //查询所有角色
    PageListRes getRoles(QueryVo queryVo);
    //保存新的角色以及其对应的权限
    void saveRole(Role role);
    //更新新的角色以及其对应的权限
    void updateRole(Role role);
    //删除角色以及权限
    void deleteRole(Long rid);
    //获取角色列表
    List<Role> roleList();
    ///*根据用户的id来获取角色*/
    List<Long> getRoleByEid(Long id);
}
