package com.cy.controller.personnel;

import com.cy.pojo.EmpPosAdjust;
import com.cy.pojo.Position;
import com.cy.pojo.RespBean;
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
@RequestMapping("/personnel/pos-adjust")
public class PosAdjustController {

    @Autowired
    private IPositionService positionService;


    @Autowired
    private IEmpPosAdjustService empPosAdjustService;

    /**
     * 获取职位列表
     * @return
     */
    @ApiOperation(value = "获取职位列表")
    @GetMapping("/position")
    public RespBean getPositionAll(){
        List<Position> list = positionService.list();
        return RespBean.ok(list);
    }

    /**
     * 获取员工职位调动信息
     * @param currentPage
     * @param size
     * @param empPosAdjust
     * @return
     */
    @ApiOperation(value = "获取员工职位调动信息")
    @GetMapping("/")
    public RespBean getTransferByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      EmpPosAdjust empPosAdjust) {
        return empPosAdjustService.getPosAdjustByPage(currentPage, size, empPosAdjust);
    }

    /**
     * 删除调动信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除调动信息")
    @DeleteMapping("/delete")
    public RespBean deleteTransferByIds(@RequestParam List<Integer> ids){
        return empPosAdjustService.deletePosAdjustByIds(ids);
    }

    /**
     * 添加调动信息
     * @param empPosAdjust
     * @return
     */
    @ApiOperation(value = "添加调动信息")
    @PostMapping("/")
    public RespBean addTransfer(@RequestBody EmpPosAdjust empPosAdjust){
        return empPosAdjustService.addPosAdjust(empPosAdjust);
    }

    /**
     * 修改调动信息
     * @param empPosAdjust
     * @return
     */
    @ApiOperation(value = "修改调动信息")
    @PutMapping("/")
    public RespBean editTransfer(@RequestBody EmpPosAdjust empPosAdjust){
        return empPosAdjustService.editPosAdjust(empPosAdjust);
    }

    /**
     * 导出员工职位调动信息
     * @param response
     * @param empName
     */
    @ApiOperation(value = "导出员工职位调动信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @RequestParam(required = false) String empName){
        empPosAdjustService.export(response,empName);
    }

}
