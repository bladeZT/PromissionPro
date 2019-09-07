package com.blade.service;

import com.blade.domain.AjaxRes;
import com.blade.domain.Menu;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;

import java.util.List;

public interface MenuService {
    PageListRes getMenuList(QueryVo queryVo);
    //加载父菜单列表
    List<Menu> parentList();

    void saveMenu(Menu menu);

    AjaxRes updateMenu(Menu menu);

    AjaxRes deleteMenu(Long id);
    //获得树形菜单的数据
    List<Menu> getTreeData();
}
