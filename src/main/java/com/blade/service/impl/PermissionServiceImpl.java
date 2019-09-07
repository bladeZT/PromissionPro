package com.blade.service.impl;

import com.blade.domain.Permission;
import com.blade.mapper.PermissionMapper;
import com.blade.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    //注入mapper
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> getPermissions() {
        List<Permission> permissions = permissionMapper.selectAll();
        return permissions;
    }

    @Override
    public List<Permission> getPermissionByRid(Long rid) {

        return permissionMapper.selectPermissionByRid(rid);
    }
}
