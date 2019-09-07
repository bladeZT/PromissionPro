package com.blade.mapper;

import com.blade.domain.Employee;
import com.blade.domain.QueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll(QueryVo queryVo);

    int updateByPrimaryKey(Employee record);

    //根据id，更新员工的离职状态
    void updateState(Long id);
    //保存该员工对应的角色信息
    void insertEmployeeAndRoleRel(@Param("id") Long id, @Param("rid") Long rid);

    void deleteRoleRel(Long id);

    Employee getEmployeeWithUserName(String username);
    //根据id查询用户对应的角色编号名称
    List<String> getRolesById(Long id);
    //根据id查询用户对应的权限名称
    List<String> getPermissionById(Long id);
}