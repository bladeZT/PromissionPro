package com.blade.mapper;

import com.blade.domain.Department;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    Department selectByPrimaryKey(Long id);

    List<Department> selectAll();

    int updateByPrimaryKey(Department record);
}