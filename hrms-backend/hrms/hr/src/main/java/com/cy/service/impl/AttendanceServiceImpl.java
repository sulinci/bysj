package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.enums.AttendanceStatusEnum;
import com.cy.exception.ServiceException;
import com.cy.mapper.AttendanceMapper;
import com.cy.mapper.EmployeeMapper;
import com.cy.pojo.*;
import com.cy.pojo.vo.AttendanceVO;
import com.cy.service.IAttendanceService;
import com.cy.utils.DatetimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cy.enums.AttendanceStatusEnum.*;

/**
 * @author cy
 * @version 1.0
 */
@Service
@Slf4j
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

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
    @Override
    public RespBean list(Integer currentPage, Integer size, String name, String month) {
        Page<Employee> config = new Page<>(currentPage, size);
        // 解决当搜索条件为空时，默认查询所有数据
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        IPage<Employee> employeeIPage = employeeMapper.selectEmpAtt(config, name);
        // 如果没有指明月份，就默认显示当前月份的考勤数据
        if (month == null) {
            month = DateUtil.format(new java.util.Date(), "yyyyMM");
        }
        // 每页展示的数据
        List<Employee> employeeList = employeeIPage.getRecords();
        String[] monthDayList = DatetimeUtil.getMonthDayList(month);
        for (Employee employee : employeeList) {
            // 获取当前月的日期，格式为yyyyMMdd
            List<Attendance> attendanceList = new ArrayList<>();
            for (String day : monthDayList) {
                Attendance attendance = attendanceMapper.findByEmpId(employee.getId(), day);
                setAttendance(employee, attendanceList, day, attendance);
            }
            employee.setAttendanceList(attendanceList);
        }
        // 将响应数据填充到map中
        Map map = new HashMap();
        map.put("pages", employeeIPage.getPages());
        map.put("total", employeeIPage.getTotal());
        map.put("list", employeeList);
        map.put("dayNum", monthDayList.length);
        map.put("month", month);
        return RespBean.ok(map);
    }

    private void setAttendance(Employee employee, List<Attendance> attendanceList, String day, Attendance attendance) {
        // 如果考勤数据不存在，就重新设置数据
        if (attendance == null) {
            Date date = DateUtil.parse(day, "yyyyMMdd").toSqlDate();
            Attendance temp = new Attendance();
            // 如果是周末就休息
            if (DateUtil.isWeekend(date)) {
                temp.setStatus(AttendanceStatusEnum.REST);
            } else {
                temp.setStatus(NORMAL);
            }
            temp.setAttendanceDate(date);
            temp.setEId(employee.getId());
            attendanceList.add(temp);
        } else {
            attendanceList.add(attendance);
        }
    }
//    if (StrUtil.isEmpty(name)) {
//        name = "";
//    }
    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    @Override
    public RespBean editAttendance(Attendance attendance) {
        LambdaQueryWrapper<Attendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attendance::getEId,attendance.getEId())
                .eq(Attendance::getAttendanceDate,attendance.getAttendanceDate());
        if (saveOrUpdate(attendance,queryWrapper)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 导出员工月份考勤信息
     * @param response
     * @param name
     */
    @Override
    public void export(HttpServletResponse response, String month, String name) {
        List<AttendanceVO> list = attendanceMapper.getAttendanceVOs(name);
        for (AttendanceVO attendanceVO : list) {
            attendanceVO.setLateCount(attendanceMapper.countTimes(attendanceVO.getId(),
                    LATE.getCode(), month));
            attendanceVO.setLeaveEarlyCount(attendanceMapper.countTimes(attendanceVO.getId(),
                    LEAVE_EARLY.getCode(), month));
            attendanceVO.setAbsenteeismCount(attendanceMapper.countTimes(attendanceVO.getId(),
                    AttendanceStatusEnum.ABSENTEEISM.getCode(), month));
            attendanceVO.setLeaveCount(attendanceMapper.countTimes(attendanceVO.getId(),
                    AttendanceStatusEnum.LEAVE.getCode(), month));
        }
        String yearMonth = month.substring(0, 4) + "年" + month.substring(4) + "月";
        ExportParams params = new ExportParams(yearMonth + "考勤报表",yearMonth + "考勤报表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,AttendanceVO.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(yearMonth + "考勤报表.xls","UTF-8"));
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != outputStream){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导入员工考勤信息
     * @param file
     * @return
     */
    @Override
    @Transactional
    public RespBean importAttendance(MultipartFile file) {
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);
        try {
            List<Attendance> list = ExcelImportUtil.importExcel(file.getInputStream(), Attendance.class, params);
            for (Attendance attendance : list) {
                LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Employee::getName, attendance.getName())
                        .eq(Employee::getWorkId, attendance.getWorkId());
                attendance.setEId(employeeMapper.selectOne(queryWrapper).getId());
                //判断是否有考勤状态
                if (attendance.getStatusFlag() == null){
                    //判断是否旷工
                    if (isAbsenteeism(attendance)){
                        attendance.setStatus(AttendanceStatusEnum.ABSENTEEISM);
                    }else if (isLeaveEarly(attendance)){//判断是否早退
                        attendance.setStatus(LEAVE_EARLY);
                    }else if (isLate(attendance)){//判断是否迟到
                        attendance.setStatus(LATE);
                    }else {
                        attendance.setStatus(NORMAL);
                    }
                }else {
                    attendance.setStatus(getStatus(attendance.getStatusFlag()));
                }
                LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Attendance::getEId, attendance.getEId())
                        .eq(Attendance::getAttendanceDate, attendance.getAttendanceDate());
                if (!attendanceService.saveOrUpdate(attendance,wrapper)){
                    throw new ServiceException("数据导入失败");
                }
            }
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入数据可能存在问题，导入失败");
    }
//    list.forEach(attendance ->{
//        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getName, attendance.getName())
//                .eq(Employee::getWorkId, attendance.getWorkId());
//        attendance.setEId(employeeMapper.selectOne(queryWrapper).getId());
//        if (attendance.getStatus() == null){
//            if (isAbsenteeism(attendance)){
//                attendance.setStatus(AttendanceStatusEnum.ABSENTEEISM);
//            }else if (isLeaveEarly(attendance)){
//                attendance.setStatus(AttendanceStatusEnum.LEAVE_EARLY);
//            }else if (isLate(attendance)){
//                attendance.setStatus(AttendanceStatusEnum.LATE);
//            }else {
//                attendance.setStatus(AttendanceStatusEnum.NORMAL);
//            }
//        }
//
//    });

    private AttendanceStatusEnum getStatus(Integer statusFlag){
        switch (statusFlag){
            case 0: return NORMAL;
            case 1: return LATE;
            case 2: return LEAVE_EARLY;
            case 3: return ABSENTEEISM;
            case 4: return REST;
            case 5: return LEAVE;
            default: return null;
        }
    }

    /**
     * 是否旷工
     * @param attendance
     * @return
     */
    private boolean isAbsenteeism(Attendance attendance) {
        if (attendance.getAftTime() == null && attendance.getAftTime() == null){
            return true;
        }
        if (isLate(attendance) && isLeaveEarly(attendance)){
            return true;
        }
        return false;
    }

    //早上打卡
    @Value("${att.morning}")
    private String attMorning;

    //下午打卡
    @Value("${att.afternoon}")
    private String attAfternoon;

    /**
     * 是否早退
     * @param attendance
     * @return
     */
    private boolean isLeaveEarly(Attendance attendance) {
        if (StrUtil.isEmpty(attendance.getAftTime()) || attendance.getAftTime().toString().compareTo(attAfternoon) < 0){
            return true;
        }
        return false;
    }


    /**
     * 是否迟到
     * @param attendance
     * @return
     */
    private boolean isLate(Attendance attendance) {
        if (StrUtil.isEmpty(attendance.getMorTime()) || attendance.getMorTime().toString().compareTo(attMorning) > 0){
            return true;
        }
        return false;
    }


}
