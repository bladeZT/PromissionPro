package com.blade.mapper;

import com.blade.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(Role record);

    Role selectByPrimaryKey(Long rid);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
    //保存角色与权限之间的关系
    void insertRoleAndPermissionRel(@Param("rid") Long rid, @Param("pid") Long pid);
    //根据角色rid，打破角色与权限之间的关系
    void deleteRoleAndPermissionRel(Long rid);
    //根据用户id，查询对应的角色的rid
    List<Long> getRoleWithId(Long id);
}