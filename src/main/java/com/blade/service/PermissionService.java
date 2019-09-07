package com.blade.service;

import com.blade.domain.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getPermissions();
    /*根据角色查询对应的权限*/
    List<Permission> getPermissionByRid(Long rid);
}
