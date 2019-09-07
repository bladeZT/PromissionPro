package com.blade.service.impl;

import com.blade.domain.AjaxRes;
import com.blade.domain.Menu;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import com.blade.mapper.MenuMapper;
import com.blade.service.MenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;


    @Override
    public PageListRes getMenuList(QueryVo queryVo) {
        //调用分页
        Page<Object> page = PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
        List<Menu> menus = menuMapper.selectAll();
        PageListRes pageListRes = new PageListRes();
        pageListRes.setTotal(page.getTotal());
        pageListRes.setRows(menus);
        return pageListRes;
    }

    @Override
    public List<Menu> parentList() {
        return menuMapper.selectAll();
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public AjaxRes updateMenu(Menu menu) {
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();

        //先取出当前选择父菜单的id
        Long id = menu.getParent().getId();
        //查询出以该parentId为id的对应的menu的parentId
        Long parentId = menuMapper.selectParentId(id);
        //判断设置的父菜单是不是已经是当前menu的子菜单
        if(menu.getId() == parentId){
            ajaxRes.setMsg("不能设置自己的子菜单为自己的父菜单");
            ajaxRes.setSuccess(false);
            return ajaxRes;
        }

        try {
            menuMapper.updateByPrimaryKey(menu);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("更新失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    @Override
    public AjaxRes deleteMenu(Long id) {
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();

        try {
            //要先删除关系，把父id的外键关打破
            menuMapper.updateMenuRel(id);
            //删除记录
            menuMapper.deleteByPrimaryKey(id);
            ajaxRes.setMsg("删除成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("删除失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    @Override
    public List<Menu> getTreeData() {
        return menuMapper.getTreeData();
    }
}
