package com.blade.service.impl;

import com.blade.domain.Department;
import com.blade.mapper.DepartmentMapper;
import com.blade.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepartmentList() {
        List<Department> departments = departmentMapper.selectAll();

        return departments;
    }
}
