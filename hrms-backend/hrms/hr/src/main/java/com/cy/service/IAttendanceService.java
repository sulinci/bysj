package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.Attendance;
import com.cy.pojo.RespBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @version 1.0
 */
public interface IAttendanceService extends IService<Attendance> {
    /**
     * 分页查询员工考勤信息
     * @param currentPage
     * @param size
     * @param name
     * @param month
     * @return
     */
    RespBean list(Integer currentPage, Integer size, String name, String month);

    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    RespBean editAttendance(Attendance attendance);

    /**
     * 导出员工月份考勤信息
     * @param response
     * @param name
     */
    void export(HttpServletResponse response, String month, String name);

    /**
     * 导入员工考勤信息
     * @param file
     * @return
     */
    RespBean importAttendance(MultipartFile file);
}
