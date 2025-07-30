package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {




    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username = #{username}")
    Employee getByUsername(String username);

    //添加员工

    @Insert("insert into sky_take_out.employee(id,name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "values (#{id},#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    //分页查询
    Page<Employee> selectPage(EmployeePageQueryDTO employeePageQueryDTO);

    //修改员工状态
    @AutoFill(value = OperationType.UPDATE)
    void changeStatus(Employee employee);

    //查询员工回显
    @Select("select * from sky_take_out.employee where id = #{id}")
    Employee selectById(Long id);

    //编辑员工信息
    @AutoFill(value = OperationType.UPDATE)
    void updateById(Employee employee);

    void editPassword(PasswordEditDTO passwordEditDTO);

    //从MD5变成BCrypt加密密码
    void updatePassword(Employee employee);
}
