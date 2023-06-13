package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cy.enums.AttendanceStatusEnum;
import com.cy.mapper.*;
import com.cy.pojo.*;
import com.cy.pojo.vo.SalaryStandardVO;
import com.cy.service.ISalaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private SalaryStandardMapper salaryStandardMapper;

    @Autowired
    private SocDepMapper socDepMapper;


    /**
     * 分页查询工资表情况
     * @param currentPage
     * @param size
     * @param name
     * @param month
     * @return
     */
    @Override
    public RespBean getSalaryTable(Integer currentPage, Integer size,String name, String month) {
        IPage<Salary> config = new Page<>(currentPage, size);
        IPage<Salary> page = salaryMapper.getSalaryTable(config,name,month);
        return RespBean.ok(page);
    }

    /**
     * 根据月份结算当月工资
     * @param month
     * @return
     */
    @Override
    @Transactional
    public RespBean countSalary(String month) {
        LambdaQueryWrapper<Salary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Salary::getMonth,month);
        Integer integer = salaryMapper.selectCount(queryWrapper);
        if (integer > 0){
            return RespBean.error("该月工资已结算，请勿重复结算");
        }
        List<Employee> employeeList = employeeMapper.getEmployee();
        List<Salary> list = new ArrayList<>();
        for (Employee employee : employeeList) {
            Salary salary = new Salary();
            salary.setEId(employee.getId());
            //计算迟到扣款
            countDeduct(month, employee, salary);

            SalaryStandard standard = salaryStandardMapper.getSalaryStandardByEId(employee.getId());
            salary.setBasicSalary(standard.getBaseSalary());
            salary.setBonus(standard.getBonus());
            salary.setSubsidy(standard.getSubsidy());
            SocDep socDep = socDepMapper.selectById(standard.getSocId());
            if (socDep == null){
                return RespBean.error("有员工未设置薪资标准,请先设置薪资标准");
            }
            //计算公积金社保缴纳费用
            countHousePayAddSocialPay(salary, standard, socDep);
            //计算应发工资
            BigDecimal allSalary = getAllSalary(salary, standard);
            salary.setAllSalary(allSalary);
            salary.setMonth(month);
            list.add(salary);
        }
        if(saveBatch(list)){
            return RespBean.success("结算成功");
        }else {
            return RespBean.error("结算失败");
        }
    }

    private BigDecimal getAllSalary(Salary salary, SalaryStandard standard) {
        return standard.getBaseSalary().add(standard.getBonus())
                .add(standard.getSubsidy()).subtract(salary.getLateDeduct())
                .subtract(salary.getLeaveEarlyDeduct()).subtract(salary.getAbsenteeismDeduct())
                .subtract(salary.getLeaveDeduct()).subtract(salary.getPerHousePay()).subtract(salary.getPerSocialPay());
    }

    private void countHousePayAddSocialPay(Salary salary, SalaryStandard standard, SocDep socDep) {
        BigDecimal perHousePay = socDep.getPerHouseRate().multiply(standard.getHouseBase());
        salary.setPerHousePay(perHousePay);
        BigDecimal comHousePay = socDep.getComHouseRate().multiply(standard.getHouseBase());
        salary.setComHousePay(comHousePay);
        BigDecimal perSocialPay =
                socDep.getPerMedicalRate().add(socDep.getPerPensionRate())
                        .add(socDep.getPerUnemploymentRate()).multiply(standard.getSocialBase());
        salary.setPerSocialPay(perSocialPay);
        BigDecimal comSocialPay = socDep.getComInjuryRate().add(socDep.getComMaternityRate())
                .add(socDep.getComMedicalRate()).add(socDep.getComPensionRate())
                .add(socDep.getComUnemploymentRate()).multiply(standard.getSocialBase());
        salary.setComSocialPay(comSocialPay);
    }


    /**
     * 计算扣款
     * @param month
     * @param employee
     * @param salary
     */
    private void countDeduct(String month, Employee employee, Salary salary) {
        BigDecimal lateDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(employee.getId(),
                AttendanceStatusEnum.LATE.getCode(), month) * 50);
        salary.setLateDeduct(lateDeduct);
        //计算早退扣款
        BigDecimal leaveEarlyDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(employee.getId(),
                AttendanceStatusEnum.LEAVE_EARLY.getCode(), month) * 50);
        salary.setLeaveEarlyDeduct(leaveEarlyDeduct);
        //计算旷工扣款
        BigDecimal absenteeismDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(employee.getId(),
                AttendanceStatusEnum.ABSENTEEISM.getCode(), month) * 100);
        salary.setAbsenteeismDeduct(absenteeismDeduct);
        //计算请假扣款
        BigDecimal leaveDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(employee.getId(),
                AttendanceStatusEnum.LEAVE.getCode(), month) * 50);
        salary.setLeaveDeduct(leaveDeduct);
    }

    /**
     * 根据月份导出工资表
     * @param response
     * @param month
     */
    @Override
    public void export(HttpServletResponse response,String month) {
        List<Salary> list = salaryMapper.getSalaryByMonth(month);
        String yearMonth = month.substring(0, 4) + "年" + month.substring(4) + "月";
        ExportParams params = new ExportParams(yearMonth + "工资报表",yearMonth + "工资报表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,Salary.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(yearMonth + "工资报表.xls","UTF-8"));
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



    //        List<Salary> list = employeeList.stream().map((item) -> {
//            Salary salary = new Salary();
//            salary.setEId(item.getId());
//            //计算迟到扣款
//            BigDecimal lateDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(item.getId(),
//                    AttendanceStatusEnum.LATE.getCode(), month) * 50);
//            salary.setLateDeduct(lateDeduct);
//            //计算早退扣款
//            BigDecimal leaveEarlyDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(item.getId(),
//                    AttendanceStatusEnum.LEAVE_EARLY.getCode(), month) * 50);
//            salary.setLeaveEarlyDeduct(leaveEarlyDeduct);
//            //计算旷工扣款
//            BigDecimal absenteeismDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(item.getId(),
//                    AttendanceStatusEnum.ABSENTEEISM.getCode(), month) * 100);
//            salary.setAbsenteeismDeduct(absenteeismDeduct);
//            //计算请假扣款
//            BigDecimal leaveDeduct = BigDecimal.valueOf(attendanceMapper.countTimes(item.getId(),
//                    AttendanceStatusEnum.LEAVE.getCode(), month) * 50);
//            salary.setLeaveDeduct(leaveDeduct);
//            //计算公积金社保缴纳费用
//            SalaryStandard standard = salaryStandardMapper.getSalaryStandardByEId(item.getId());
//
//            salary.setBasicSalary(standard.getBaseSalary());
//            salary.setBonus(standard.getBonus());
//            salary.setSubsidy(standard.getSubsidy());
//            SocDep socDep = socDepMapper.selectById(standard.getSocId());
//            BigDecimal perHousePay = socDep.getPerHouseRate().multiply(standard.getHouseBase());
//            salary.setPerHousePay(perHousePay);
//            BigDecimal comHousePay = socDep.getComHouseRate().multiply(standard.getHouseBase());
//            salary.setComHousePay(comHousePay);
//            BigDecimal perSocialPay =
//                    socDep.getPerMedicalRate().add(socDep.getPerPensionRate())
//                            .add(socDep.getPerUnemploymentRate()).multiply(standard.getSocialBase());
//            salary.setPerSocialPay(perSocialPay);
//            BigDecimal comSocialPay = socDep.getComInjuryRate().add(socDep.getComMaternityRate())
//                    .add(socDep.getComMedicalRate()).add(socDep.getComPensionRate())
//                    .add(socDep.getComUnemploymentRate()).multiply(standard.getSocialBase());
//            salary.setComSocialPay(comSocialPay);
//            //计算应发工资
//            BigDecimal allSalary = standard.getBaseSalary().add(standard.getBonus())
//                    .add(standard.getSubsidy()).subtract(lateDeduct)
//                    .subtract(leaveEarlyDeduct).subtract(absenteeismDeduct)
//                    .subtract(leaveDeduct).subtract(perHousePay).subtract(perSocialPay);
//            salary.setAllSalary(allSalary);
//            salary.setMonth(month);
//            return salary;
//        }).collect(Collectors.toList());


}
