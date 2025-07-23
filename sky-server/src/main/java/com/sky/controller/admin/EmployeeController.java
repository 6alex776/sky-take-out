package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import com.sky.dto.EmployeePageQueryDTO;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关注解")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //封装对象
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    //添加员工
    @PostMapping
    @ApiOperation("新增员工")
    public Result insert(@RequestBody EmployeeDTO employeeDTO) {

        log.info("添加员工{}",employeeDTO);

        employeeService.inert(employeeDTO);

        return Result.success();
    }

    //分页查询
    @GetMapping(value = "/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> selectPage(EmployeePageQueryDTO employeePageQueryDTO) {

        if (employeePageQueryDTO.getPage() <= 0) {
            employeePageQueryDTO.setPage(1);
        }
        if (employeePageQueryDTO.getPageSize() <= 0) {
            employeePageQueryDTO.setPageSize(10);
        }

        log.info("查询页面{}",employeePageQueryDTO);

        PageResult pageResult = employeeService.selectPage(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    //查询类加泛型，非查询类可不加
    //修改员工状态
    @PostMapping(value = "/status/{status}")
    @ApiOperation("更改员工状态")
    public Result changeStatus(@PathVariable Integer status,Long id){

        if (status == null) {
            status = 1;
        }

        log.info("修改员工状态");

        employeeService.changeStatus(status,id);

        return Result.success();
    }

    //查询员工回显
    @GetMapping(value = "/{id}")
    @ApiOperation("查询员工回显")
    public Result<Employee> selectById(@PathVariable Integer id){

        log.info("查询员工{}",id);

        Employee employee = employeeService.selectById(id);

        return Result.success(employee);
    }

    //编辑员工信息
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result updateById(@RequestBody Employee employee){

        log.info("编辑员工信息{}",employee);

        employeeService.updateById(employee);

        return Result.success();
    }
}
