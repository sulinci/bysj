package com.cy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询所有员工（分页）
     * @param page
     * @param employee
     * @return
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee);


    IPage<Employee> selectEmpAtt(Page<Employee> config, String name);

    List<Employee> getEmployee();

    /**
     * 根据姓名获取员工信息
     * @param name
     * @return
     */
    List<Employee> getEmployees(String name);
}
