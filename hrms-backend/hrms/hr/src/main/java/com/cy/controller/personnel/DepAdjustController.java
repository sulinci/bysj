package com.cy.controller.personnel;

import com.cy.pojo.*;
import com.cy.service.IDepartmentService;
import com.cy.service.IEmpDepAdjustService;
import com.cy.service.IEmpPosAdjustService;
import com.cy.service.IPositionService;
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
@RequestMapping("/personnel/dep-adjust")
public class DepAdjustController {

    @Autowired
    private IDepartmentService departmentService;


    @Autowired
    private IEmpDepAdjustService empDepAdjustService;

    /**
     * 获取部门列表
     * @return
     */
    @ApiOperation(value = "获取部门列表")
    @GetMapping("/department")
    public RespBean getDepartmentAll(){
        return departmentService.getAllDepartments();
    }

    /**
     * 获取员工部门调动信息
     * @param currentPage
     * @param size
     * @param empDepAdjust
     * @return
     */
    @ApiOperation(value = "获取员工部门调动信息")
    @GetMapping("/")
    public RespBean getTransferByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      EmpDepAdjust empDepAdjust) {
        return empDepAdjustService.getDepAdjustByPage(currentPage, size, empDepAdjust);
    }

    /**
     * 删除员工部门调动信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除员工部门调动信息")
    @DeleteMapping("/delete")
    public RespBean deleteTransferByIds(@RequestParam List<Integer> ids){
        return empDepAdjustService.deleteDepAdjustByIds(ids);
    }

    /**
     * 添加员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    @ApiOperation(value = "添加员工部门调动信息")
    @PostMapping("/")
    public RespBean addTransfer(@RequestBody EmpDepAdjust empDepAdjust){
        return empDepAdjustService.addDepAdjust(empDepAdjust);
    }

    /**
     * 修改员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    @ApiOperation(value = "修改员工部门调动信息")
    @PutMapping("/")
    public RespBean editTransfer(@RequestBody EmpDepAdjust empDepAdjust){
        return empDepAdjustService.editDepAdjust(empDepAdjust);
    }

    /**
     * 导出员工部门调动信息
     * @param response
     * @param empName
     */
    @ApiOperation(value = "导出员工部门调动信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @RequestParam(required = false) String empName){
        empDepAdjustService.export(response,empName);
    }

}
