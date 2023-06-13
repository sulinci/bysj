package com.cy.controller.personnel;

import com.cy.pojo.Attendance;
import com.cy.pojo.RespBean;
import com.cy.service.IAttendanceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @version 1.0
 */
@RestController
@RequestMapping("/personnel/attendance")
public class PerAttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    /**
     * 分页查询员工考勤信息
     * @param currentPage
     * @param size
     * @param name
     * @param month
     * @return
     */
    @ApiOperation(value = "分页查询员工考勤信息")
    @GetMapping
    public RespBean list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size, String name, String month){
        return attendanceService.list(currentPage,size,name,month);
    }

    /**
     * 修改员工考勤信息
     * @param attendance
     * @return
     */
    @ApiOperation(value = "修改员工考勤信息")
    @PutMapping("/edit")
    public RespBean editAttendance(@RequestBody Attendance attendance){
        return  attendanceService.editAttendance(attendance);
    }

    /**
     * 导出员工月份考勤信息
     * @param response
     * @param name
     */
    @ApiOperation(value = "导出员工月份考勤信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void export(HttpServletResponse response,String month,
                       @RequestParam(required = false) String name){
        attendanceService.export(response, month, name);
    }

    /**
     * 导入考勤数据
     * @param file
     * @return
     */
    @ApiOperation(value = "导入考勤数据")
    @ApiImplicitParams({@ApiImplicitParam(name="file",value = "上传文件",dataType = "MultipartFile")})
    @PostMapping("/import")
    public RespBean importAttendance(MultipartFile file){
        return attendanceService.importAttendance(file);
    }
}
