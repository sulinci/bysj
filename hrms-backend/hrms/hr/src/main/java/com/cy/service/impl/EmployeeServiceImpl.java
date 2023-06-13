package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.enums.AttendanceStatusEnum;
import com.cy.mapper.EmployeeMapper;
import com.cy.pojo.*;
import com.cy.service.IAttendanceService;
import com.cy.service.IDepartmentService;
import com.cy.service.IEmployeeService;
import com.cy.service.IPositionService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Autowired
//    private MailLogMapper mailLogMapper;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IAttendanceService attendanceService;

    /**
     * 获取所有员工（分页）
     *
     * @param currentPage
     * @param size
     * @param employee
     * @return
     */
    @Override
    public RespBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee) {
        //开启分页
        Page<Employee> page = new Page<>(currentPage, size);
        //查询员工信息
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeByPage(page, employee);
        //对查询信息进行处理
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(), employeeIPage.getRecords());
        //返回处理消息
        return RespBean.ok(respPageBean);
    }

    /**
     * 获取当前最大工号
     *
     * @return
     */
    @Override
    public RespBean maxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return RespBean.success(null, String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1));
    }

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */
    @Override
    public RespBean insertEmployee(Employee employee) {
        if(employeeMapper.insert(employee) > 0){
            employee.setWorkId("emp_" + employee.getId());
            employeeMapper.updateById(employee);
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }



    /**
     * 导出员工信息
     * @param response
     * @param name
     */
    @Override
    public void exportEmployee(HttpServletResponse response, String name) {
        List<Employee> list = employeeMapper.getEmployees(name);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,Employee.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
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
     * 导入员工信息
     * @param file
     * @return
     */
    @Override
    public RespBean importEmployee(MultipartFile file) {
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);

        List<Position> positions = positionService.list();
        List<Department> departments = departmentService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee ->{
                //设置部门id
                employee.setDepId(departments.get(departments.indexOf(new Department(employee.getDepName()))).getId());
                //设置职位id
                employee.setPosId(positions.get(positions.indexOf(new Position(employee.getPosName()))).getId());
            });
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入数据可能存在问题，导入失败");
    }

    @Override
    public RespBean getCountData() {
        //获取总员工数
        int totalNum = employeeService.count();
        Date datetime = new Date(System.currentTimeMillis());
        String day = DateUtil.format(datetime, "yyyy-MM-dd");
        //获取请假人数
        int leaveCount = attendanceService.count(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getAttendanceDate, day).eq(Attendance::getStatus, AttendanceStatusEnum.LEAVE.getCode()));
        //获取迟到人数
        int lateCount = attendanceService.count(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getAttendanceDate, day).eq(Attendance::getStatus, AttendanceStatusEnum.LATE.getCode()));
        //获取早退人数
        int leaveEarlyCount = attendanceService.count(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getAttendanceDate, day).eq(Attendance::getStatus, AttendanceStatusEnum.LEAVE_EARLY.getCode()));
        //获取旷工人数
        int absenteeismCount = attendanceService.count(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getAttendanceDate, day).eq(Attendance::getStatus, AttendanceStatusEnum.ABSENTEEISM.getCode()));
        //获取状态正常人数
        int normalNum = totalNum - lateCount - leaveEarlyCount - leaveCount - absenteeismCount;
        Map<String, Object> map = new HashMap<>();
        map.put("totalNum",totalNum);
        map.put("normalNum",normalNum);
        map.put("leaveCount",leaveCount);
        map.put("lateCount",lateCount);
        map.put("leaveEarlyCount",leaveEarlyCount);
        map.put("absenteeismCount",absenteeismCount);
        return RespBean.ok(map);
    }

    @Override
    public RespBean getEmpData() {
        QueryWrapper queryWrapper = new QueryWrapper<Employee>().ge("create_time", DateUtil.thisYear() + "-01-01");
        List<Employee> list = this.employeeService.list(queryWrapper);
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (Employee employee : list) {
            Quarter quarter = DateUtil.quarterEnum(employee.getCreateTime());
            // 统计每个季度的注册人数
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return RespBean.ok(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}
