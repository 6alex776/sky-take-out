package com.smartdine.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartdine.constant.MessageConstant;
import com.smartdine.constant.PasswordConstant;
import com.smartdine.constant.StatusConstant;
import com.smartdine.context.BaseContext;
import com.smartdine.dto.EmployeeDTO;
import com.smartdine.dto.EmployeeLoginDTO;
import com.smartdine.dto.EmployeePageQueryDTO;
import com.smartdine.dto.PasswordEditDTO;
import com.smartdine.entity.Employee;
import com.smartdine.exception.AccountLockedException;
import com.smartdine.exception.AccountNotFoundException;
import com.smartdine.exception.PasswordErrorException;
import com.smartdine.mapper.EmployeeMapper;
import com.smartdine.result.PageResult;
import com.smartdine.service.EmployeeService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

import com.smartdine.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String rawPassword = employeeLoginDTO.getPassword();

        // 1. 查询员工
        Employee employee = employeeMapper.getByUsername(username);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2. 判断密码类型并验�?
        String storedPassword = employee.getPassword();
        boolean passwordMatch = false;

        // 2.1 判断是否为MD5格式�?2�?6进制字符串）
        if (storedPassword.length() == 32 && storedPassword.matches("[0-9a-f]{32}")) {
            // MD5验证
            String md5HashedPassword = DigestUtils.md5DigestAsHex(rawPassword.getBytes());
            passwordMatch = md5HashedPassword.equals(storedPassword);

            // 验证成功后，将MD5密码迁移为jbcrypt
            if (passwordMatch) {
                String newHashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());//TODO jbcrypt密码加密
                employee.setPassword(newHashedPassword);
                employeeMapper.updatePassword(employee); // 需要新增一个更新密码的Mapper方法
            }
        }
        // 2.2 判断是否为jbcrypt格式（以$2a$开头）
        else if (storedPassword.startsWith("$2a$")) {
            passwordMatch = BCrypt.checkpw(rawPassword, storedPassword);
        }
        else {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3. 验证密码结果
        if (!passwordMatch) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 4. 验证账号状�?
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }


    //添加员工
    @Override
    public void inert(Employee employee) {


        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        employeeMapper.insert(employee);

    }

    //分页查询
    @Override
    public PageResult selectPage(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.selectPage(employeePageQueryDTO);


        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;

    }

    //修改员工状�?
    @Override
    public void changeStatus(Integer status, Long id) {

        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.changeStatus(employee);

    }

    //查询员工回显
    @Override
    public Employee selectById(Long id) {

        return employeeMapper.selectById(id);
    }

    //编辑员工信息
    @Override
    public void updateById(Employee employee) {

//        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.updateById(employee);
    }

    //修改员工密码
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {

        Long currentEmpId = BaseContext.getCurrentId();//TODO 获取当前用户id
        log.info("当前操作人员ID：{}", currentEmpId);
        passwordEditDTO.setEmpId(currentEmpId);

        // 1. 查询员工
        Employee employee = employeeMapper.selectById(currentEmpId);
        String storedPassword = employee.getPassword();
        String rawOldPassword = passwordEditDTO.getOldPassword();

        // 2. 判断密码类型并验证旧密码
        boolean oldPasswordMatch = false;

        if (storedPassword.length() == 32 && storedPassword.matches("[0-9a-f]{32}")) {
            // MD5验证
            String md5HashedOldPassword = DigestUtils.md5DigestAsHex(rawOldPassword.getBytes());
            oldPasswordMatch = md5HashedOldPassword.equals(storedPassword);
        }
        else if (storedPassword.startsWith("$2a$")) {
            // jbcrypt验证
            oldPasswordMatch = BCrypt.checkpw(rawOldPassword, storedPassword);
        }
        else {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (!oldPasswordMatch) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3. 使用jbcrypt加密新密码（无论旧密码是MD5还是jbcrypt�?
        String newHashedPassword = BCrypt.hashpw(passwordEditDTO.getNewPassword(), BCrypt.gensalt());
        passwordEditDTO.setNewPassword(newHashedPassword);

        // 4. 更新密码
        employeeMapper.editPassword(passwordEditDTO);
    }
}
