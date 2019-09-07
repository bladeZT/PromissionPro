package com.blade.service;

import com.blade.domain.Employee;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EmployeeService {
    //查询全部员工
    PageListRes getEmployee(QueryVo queryVo);
    //保存一个用户
    void saveEmployee(Employee employee);
    //更新编辑一个员工
    void updateEmployee(Employee employee);
    //更新员工的离职状态
    void updateState(Long id);
    //到数据库中查询有没有当前用户
    Employee getEmployeeWithUserName(String username);
    //从数据库中查询用户的角色的编号名称
    List<String> getRolesById(Long id);
    //根据用户的id，查询权限的名称
    List<String> getPermissionById(Long id);
}
