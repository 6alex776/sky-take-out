package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    //分页查询
    PageResult selectPage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    void inert(Employee employee);


    void changeStatus(Integer status, Long id);


    Employee selectById(Integer id);


    void updateById(Employee employee);
}
