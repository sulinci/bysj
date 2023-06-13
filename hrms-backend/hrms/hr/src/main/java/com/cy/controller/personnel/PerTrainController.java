package com.cy.controller.personnel;

import com.cy.pojo.EmployeeTrain;
import com.cy.pojo.RespBean;
import com.cy.service.IEmployeeTrainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
@RestController
@RequestMapping("/personnel/train")
public class PerTrainController {
    @Autowired
    private IEmployeeTrainService employeeTrainService;

    /**
     * 获取培训信息
     * @param currentPage
     * @param size
     * @param employeeTrain
     * @return
     */
    @ApiOperation(value = "获取培训信息")
    @GetMapping("/")
    public RespBean getTrainByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   EmployeeTrain employeeTrain){
        return employeeTrainService.getTrainByPage(currentPage,size,employeeTrain);
    }

    /**
     * 删除培训信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除培训信息")
    @DeleteMapping("/delete")
    public RespBean deleteTrainByIds(@RequestParam List<Integer> ids){
        return employeeTrainService.deleteTrainByIds(ids);
    }

    /**
     * 添加培训信息
     * @param employeeTrain
     * @return
     */
    @ApiOperation(value = "添加培训信息")
    @PostMapping()
    public RespBean addTrain(@RequestBody EmployeeTrain employeeTrain){
        return employeeTrainService.addTrain(employeeTrain);
    }

    /**
     * 修改培训信息
     * @param employeeTrain
     * @return
     */
    @ApiOperation(value = "修改培训信息")
    @PutMapping
    public RespBean editTrain(@RequestBody EmployeeTrain employeeTrain){
        return employeeTrainService.editTrain(employeeTrain);
    }

    /**
     * 导出培训信息
     * @param response
     * @param empName
     */
    @ApiOperation(value = "导出培训信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @RequestParam(required = false) String empName){
        employeeTrainService.export(response,empName);
    }
}
