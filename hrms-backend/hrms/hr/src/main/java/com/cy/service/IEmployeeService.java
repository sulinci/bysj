package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.Employee;
import com.cy.pojo.RespBean;
import com.cy.pojo.RespPageBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @return
     */
    RespBean getEmployeeByPage(Integer currentPage, Integer size , Employee employee);

    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean insertEmployee(Employee employee);

//    /**
//     * 查询员工
//     * @param id
//     * @return
//     */
//    List<Employee> getEmployee(Integer id);


    /**
     * 导出员工信息
     * @param response
     * @param name
     */
    void exportEmployee(HttpServletResponse response, String name);

    /**
     * 导入员工信息
     * @param file
     * @return
     */
    RespBean importEmployee(MultipartFile file);

    RespBean getCountData();

    RespBean getEmpData();

}
