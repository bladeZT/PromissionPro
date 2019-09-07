package com.blade.service.impl;

import com.blade.domain.PageListRes;
import com.blade.domain.Permission;
import com.blade.domain.QueryVo;

import com.blade.domain.Role;
import com.blade.mapper.RoleMapper;
import com.blade.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    //注入dao层
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageListRes getRoles(QueryVo queryVo) {
        //调用分页
        Page<Object> page = PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
        List<Role> roles = roleMapper.selectAll();
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(roles);
        return pageListRes;
    }

    @Override
    public void saveRole(Role role) {
        //保存角色
        roleMapper.insert(role);
        //保存角色与权限之间的关系
        for(Permission permission : role.getPermissions()){
            roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
        }

    }

    @Override
    public void updateRole(Role role) {
        //先打破角色和权限之间的关系
        roleMapper.deleteRoleAndPermissionRel(role.getRid());
        //更新角色
        roleMapper.updateByPrimaryKey(role);
        //重新建立角色和权限之间的关系
        for(Permission permission : role.getPermissions()){
            roleMapper.insertRoleAndPermissionRel(role.getRid(),permission.getPid());
        }
    }

    @Override
    public void deleteRole(Long rid) {
        //先打破角色和权限之间的关系,即删除与该角色关联的权限
        roleMapper.deleteRoleAndPermissionRel(rid);
        //删除角色
        roleMapper.deleteByPrimaryKey(rid);
    }

    @Override
    public List<Role> roleList() {
        List<Role> roles = roleMapper.selectAll();
        return roles;
    }

    @Override
    public List<Long> getRoleByEid(Long id) {
        return roleMapper.getRoleWithId(id);

    }
}
