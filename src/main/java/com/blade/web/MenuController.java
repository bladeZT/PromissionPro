package com.blade.web;

import com.blade.domain.*;
import com.blade.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;


    @RequestMapping("/menu")
    public String menu(){
        return "menu";
    }
    /*接收 菜单列表 的请求*/
    @RequestMapping("/menuList")
    @ResponseBody
    public PageListRes menuList(QueryVo queryVo){
        //调用业务层查询菜单列表
        PageListRes pageListRes = menuService.getMenuList(queryVo);
        return pageListRes;
    }
    /*接收 父菜单列表 的请求*/
    @RequestMapping("/parentList")
    @ResponseBody
    public List<Menu> parentList(){
        return menuService.parentList();
    }
    /*保存 新的菜单*/
    @RequestMapping("/saveMenu")
    @ResponseBody
    public AjaxRes saveMenu(Menu menu){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层，添加新菜单
            menuService.saveMenu(menu);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("保存失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*更新菜单*/
    @RequestMapping("/updateMenu")
    @ResponseBody
    public AjaxRes updateMenu(Menu menu){
        return menuService.updateMenu(menu);
    }
    /*删除菜单*/
    @RequestMapping("/deleteMenu")
    @ResponseBody
    public AjaxRes deleteMenu(Long id){
        return menuService.deleteMenu(id);
    }
    /*获取树形菜单的数据*/
    @RequestMapping("/getTreeData")
    @ResponseBody
    public List<Menu> getTreeData(){
        List<Menu> treeData = menuService.getTreeData();
        //判断一下当前用户的权限，如果没有这个权限，就从集合当中移除
        //获取用户  判断用户是不是管理员
        Subject subject = SecurityUtils.getSubject();
        //当前的用户
        Employee principal = (Employee)subject.getPrincipal();
        if(!principal.getAdmin()){
            //做权限校验
            checkPermission(treeData);
        }
        return treeData;
    }
    /*做用户的菜单权限校验*/
    public void checkPermission(List<Menu> menus){
        //获取主体
        Subject subject = SecurityUtils.getSubject();
        //遍历所有菜单及其子菜单
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()){
            Menu menu = iterator.next();
            //首先判断一下该菜单有没有权限对象
            if(menu.getPermission() != null){
                //判断当前menu是否有权限对象，如果没有，当前遍历的菜单就从集合当中移除
                String presource = menu.getPermission().getPresource();
                //判断主体里面有没有这个权限
                if(!subject.isPermitted(presource)){
                    //没有权限，把当前遍历菜单移除
                    iterator.remove();
                    continue;
                }
            }
            //判断是否有子菜单，有子菜单，子菜单也要做
            if(menu.getChildren().size()>0){
                checkPermission(menu.getChildren());

            }

        }

    }
}








