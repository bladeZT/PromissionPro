package com.blade.mapper;

import com.blade.domain.Menu;
import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    Menu selectByPrimaryKey(Long id);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);
    //根据id查询menu的parentId
    Long selectParentId(Long id);
    //把父id的外键关系打破
    void updateMenuRel(Long id);
    //获得树形目录的数据
    List<Menu> getTreeData();
}