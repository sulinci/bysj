package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.RespBean;
import com.cy.pojo.SalaryStandard;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @version 1.0
 */
public interface ISalaryStandardService extends IService<SalaryStandard> {

    /**
     * 分页查询薪资标准
     * @param currentPage
     * @param size
     * @param name
     * @return
     */
    RespBean getSalaryStandardByPage(Integer currentPage, Integer size, String name);

    /**
     * 设置薪资标准
     * @param salaryStandard
     * @return
     */
    RespBean setSalaryStandard(SalaryStandard salaryStandard);

    /**
     * 获取五险一金信息
     * @return
     */
    RespBean getSoc();

    void exportSalaryStandard(HttpServletResponse response, String name);
}
