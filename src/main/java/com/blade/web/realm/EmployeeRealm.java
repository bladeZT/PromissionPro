package com.blade.web.realm;

import com.blade.domain.Employee;
import com.blade.service.EmployeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRealm extends AuthorizingRealm {
    @Autowired
    private EmployeeService employeeService;

    /*认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("来到了认证---------");
        //获取身份信息.shiro规定了表单中的name得是username和password
        String username = (String)token.getPrincipal();
        System.out.println("username = " + username);
        //到数据库中查询有没有当前用户
        Employee employee = employeeService.getEmployeeWithUserName(username);
        System.out.println("employee = " + employee);
        if(employee == null){
            return null;
        }
        //如果有这个用户，就去做认证
        //参数： 主体（会自动把体放到session当中），正确的密码，盐，当前的realm名称
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                employee,
                employee.getPassword(),
                ByteSource.Util.bytes(employee.getUsername()),
                this.getName());
        return info;
    }

    /*授权
    *
    * 调用时期：
    *   1.发现访问路径对应的方法上面 有授权注解  就会调用
    *   2.页面当中有授权标签 也会调用
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("come授权");
        //获取用户的身份信息
        Employee employee = (Employee)principals.getPrimaryPrincipal();
        //查询角色和对应的权限
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        //判断当前用户是不是管理员，如果是管理员就拥有所有的权限
        if(employee.getAdmin()){
            //拥有所有的权限
            permissions.add("*:*");
        }else {
            //从数据库中查询角色
            roles = employeeService.getRolesById(employee.getId());
            //从数据库中查询权限
            permissions = employeeService.getPermissionById(employee.getId());
        }

        //给授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }


}
