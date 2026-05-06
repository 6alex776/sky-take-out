package com.smartdine.service;

import com.smartdine.dto.EmployeeDTO;
import com.smartdine.dto.EmployeeLoginDTO;
import com.smartdine.dto.EmployeePageQueryDTO;
import com.smartdine.dto.PasswordEditDTO;
import com.smartdine.entity.Employee;
import com.smartdine.result.PageResult;

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


    Employee selectById(Long id);


    void updateById(Employee employee);


    //修改员工密码
    void editPassword(PasswordEditDTO passwordEditDTO);
}
