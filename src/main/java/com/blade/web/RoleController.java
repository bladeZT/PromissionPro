package com.blade.web;

import com.blade.domain.AjaxRes;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import com.blade.domain.Role;
import com.blade.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {

    //注入业务层
    @Autowired
    private RoleService roleService;

    @RequestMapping("/role")
    public String role(){
        return "role";
    }
    /*接受 角色列表的 请求*/
    @RequestMapping("/getRoles")
    @ResponseBody
    public PageListRes getRoles(QueryVo queryVo){
        //调用业务层，查询角色列表
        PageListRes roles = roleService.getRoles(queryVo);
        return roles;
    }
    /*接收 新角色保存 的请求*/
    @RequestMapping("/saveRole")
    @ResponseBody
    public AjaxRes saveRole(Role role){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层，保存角色和权限
            roleService.saveRole(role);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("保存失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*接收 更新角色 的请求*/
    @RequestMapping("/updateRole")
    @ResponseBody
    public AjaxRes updateRole(Role role){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层，更新角色和权限
            roleService.updateRole(role);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("更新失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*接收 删除角色 的请求*/
    @RequestMapping("/deleteRole")
    @ResponseBody
    public AjaxRes deleteRole(Long rid){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层，删除角色和对应的权限
            roleService.deleteRole(rid);
            ajaxRes.setMsg("删除角色成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("删除角色失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*获取角色的列表*/
    @RequestMapping("/roleList")
    @ResponseBody
    public List<Role> roleList(){
        List<Role> roles = roleService.roleList();
        return roles;
    }
    /*根据用户的id来获取角色*/
    @RequestMapping("/getRoleByEid")
    @ResponseBody
    public List<Long> getRoleByEid(Long id){
        return roleService.getRoleByEid(id);
    }
}
