package com.cy.controller.salary;

import com.cy.pojo.RespBean;
import com.cy.pojo.SalaryStandard;
import com.cy.pojo.SocDep;
import com.cy.service.ISalaryStandardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @version 1.0
 */
@RestController
@RequestMapping("/salary/standard")
public class SalaryStandardController {

    @Autowired
    private ISalaryStandardService salaryStandardService;

    /**
     * 获取五险一金信息
     * @return
     */
    @GetMapping("/soc")
    public RespBean getSoc(){
        return salaryStandardService.getSoc();
    }

    /**
     * 分页查询薪资标准
     * @param currentPage
     * @param size
     * @param empName
     * @return
     */
    @GetMapping("/")
    public RespBean getSalaryStandardByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String empName){
        return salaryStandardService.getSalaryStandardByPage(currentPage,size,empName);
    }

    /**
     * 设置薪资标准
     * @param salaryStandard
     * @return
     */
    @PostMapping("/")
    public RespBean setSalaryStandard(@RequestBody SalaryStandard salaryStandard){
        return salaryStandardService.setSalaryStandard(salaryStandard);
    }

    /**
     * 导出员工薪资标准数据
     * @param response
     * @param name
     */
    @ApiOperation(value = "导出员工薪资标准数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportSalaryStandard(HttpServletResponse response, @RequestParam(required = false) String name){
        salaryStandardService.exportSalaryStandard(response,name);
    }
}
