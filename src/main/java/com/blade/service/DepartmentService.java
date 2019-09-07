package com.blade.service;

import com.blade.domain.Department;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DepartmentService {
    List<Department> getDepartmentList();
}
