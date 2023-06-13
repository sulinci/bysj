package com.cy.controller.salary;

import com.cy.pojo.RespBean;
import com.cy.pojo.Salary;
import com.cy.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
@Api(tags = "SalaryController")
@RestController
@RequestMapping("/salary/salary")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    /**
     * 分页获取员工工资信息
     * @param currentPage
     * @param size
     * @param name
     * @param month
     * @return
     */
    @ApiOperation(value = "分页获取员工工资信息")
    @GetMapping("/")
    public RespBean getSalaryTable(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String month){
        return salaryService.getSalaryTable(currentPage, size,name, month);
    }

    /**
     * 结算员工工资信息
     * @param month
     * @return
     */
    @ApiOperation(value = "结算员工工资信息")
    @PostMapping("/")
    public RespBean countSalary(String month){
        return salaryService.countSalary(month);
    }

    /**
     * 导出员工工资信息
     * @param response
     * @param month
     */
    @ApiOperation(value = "导出员工工资信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void export(HttpServletResponse response, String month){
        salaryService.export(response, month);
    }

}
