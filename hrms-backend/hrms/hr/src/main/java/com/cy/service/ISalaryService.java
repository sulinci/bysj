package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.RespBean;
import com.cy.pojo.Salary;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @version 1.0
 */
public interface ISalaryService extends IService<Salary> {

    /**
     * 获取工资表
     * @param currentPage
     * @param size
     * @param month
     * @return
     */
    RespBean getSalaryTable(Integer currentPage, Integer size, String name,String month);

    /**
     * 结算工资
     * @param month
     * @return
     */
    RespBean countSalary(String month);

    /**
     * 导出月工资表
     * @param month
     * @return
     */
    void export(HttpServletResponse response, String month);
}
