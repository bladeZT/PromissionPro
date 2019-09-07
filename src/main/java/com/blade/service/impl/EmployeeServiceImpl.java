package com.blade.service.impl;

import com.blade.domain.Employee;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import com.blade.domain.Role;
import com.blade.mapper.EmployeeMapper;
import com.blade.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public PageListRes getEmployee(QueryVo queryVo) {
        //调用mapper查询员工
        //配置了分页，可以使用分页查询
        Page<Object> page = PageHelper.startPage(queryVo.getPage(),queryVo.getRows());
        List<Employee> employees = employeeMapper.selectAll(queryVo);
        //封装成pageList
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(employees);
        return pageListRes;
    }

    @Override
    public void saveEmployee(Employee employee) {
        //把密码进行加密
        Md5Hash md5Hash = new Md5Hash(employee.getPassword(), employee.getUsername(), 2);
        employee.setPassword(md5Hash.toString());
        //保存用工
        employeeMapper.insert(employee);
        //保存员工对应的角色
        for(Role role : employee.getRoles()){
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        //更新员工
        employeeMapper.updateByPrimaryKey(employee);
        //删除员工与原有角色的关系
        employeeMapper.deleteRoleRel(employee.getId());
        //重新保存员工和角色的关系
        for(Role role : employee.getRoles()){
            employeeMapper.insertEmployeeAndRoleRel(employee.getId(),role.getRid());
        }
    }

    @Override
    public void updateState(Long id) {
        employeeMapper.updateState(id);
    }

    //到数据库中查询有没有当前用户
    @Override
    public Employee getEmployeeWithUserName(String username) {

        return employeeMapper.getEmployeeWithUserName(username);
    }
    /* //从数据库中查询角色的编号名称*/
    @Override
    public List<String> getRolesById(Long id) {

        return employeeMapper.getRolesById(id);
    }
    /*//根据用户的id，查询权限的名称*/
    @Override
    public List<String> getPermissionById(Long id) {

        return employeeMapper.getPermissionById(id);
    }
}
