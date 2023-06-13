package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.EmpDepAdjustMapper;
import com.cy.mapper.EmpPosAdjustMapper;
import com.cy.pojo.*;
import com.cy.service.IEmpDepAdjustService;
import com.cy.service.IEmpPosAdjustService;
import com.cy.service.IEmployeeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * @author cy
 * @version 1.0
 */
@Service
public class EmpDepAdjustServiceImpl extends ServiceImpl<EmpDepAdjustMapper, EmpDepAdjust> implements IEmpDepAdjustService {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private EmpDepAdjustMapper empDepAdjustMapper;

    /**
     * 获取员工部门调动信息
     * @param currentPage
     * @param size
     * @param empDepAdjust
     * @return
     */
    @Override
    public RespBean getDepAdjustByPage(Integer currentPage, Integer size, EmpDepAdjust empDepAdjust) {
        Page<EmpDepAdjust> page = new Page<>(currentPage, size);
        IPage<EmpDepAdjust> posAdjustIPage = empDepAdjustMapper.getDepAdjustByPage(page, empDepAdjust);
        RespPageBean respPageBean = new RespPageBean(posAdjustIPage.getTotal(),posAdjustIPage.getRecords());
        return RespBean.ok(respPageBean);
    }

    /**
     * 删除员工部门调动信息
     * @param ids
     * @return
     */
    @Override
    public RespBean deleteDepAdjustByIds(List<Integer> ids) {
        LambdaQueryWrapper<EmpDepAdjust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EmpDepAdjust::getId,ids);
        boolean flag = this.removeByIds(ids);
        if (flag){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除删除，请稍后重试");
    }

    /**
     * 添加员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    @Override
    @Transactional
    public RespBean addDepAdjust(EmpDepAdjust empDepAdjust) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getWorkId,empDepAdjust.getEmpWorkID());
        queryWrapper.eq(Employee::getName,empDepAdjust.getEmpName());
        Employee employee = employeeService.getOne(queryWrapper);
        if (employee == null){
            return RespBean.error("员工工号或姓名错误,请检查后输入");
        }
        if (employee.getDepId() == empDepAdjust.getAfterDepId()){
            return RespBean.error("员工已在该部门,请检查后输入");
        }
        empDepAdjust.setEid(employee.getId());
        empDepAdjust.setBeforeDepId(employee.getDepId());
        int i = empDepAdjustMapper.insert(empDepAdjust);
        if (i > 0){
            // 修改员工部门
            employee.setDepId(empDepAdjust.getAfterDepId());
            employeeService.updateById(employee);
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 修改员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    @Override
    public RespBean editDepAdjust(EmpDepAdjust empDepAdjust) {
        int i = empDepAdjustMapper.updateById(empDepAdjust);
        if (i > 0){
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getWorkId,empDepAdjust.getEmpWorkID());
            queryWrapper.eq(Employee::getName,empDepAdjust.getEmpName());
            Employee employee = employeeService.getOne(queryWrapper);
            //判断员工部门是否被修改
            if(employee.getDepId() != empDepAdjust.getAfterDepId()){
                // 修改员工部门
                employee.setDepId(empDepAdjust.getAfterDepId());
                employeeService.updateById(employee);
            }
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    /**
     * 导出员工部门调动信息
     * @param response
     * @param name
     */
    @Override
    public void export(HttpServletResponse response, String name) {
        List<EmpDepAdjust> list = empDepAdjustMapper.getEmpDepAdjusts(name);
        ExportParams params = new ExportParams("员工部门调动信息表","员工部门调动信息表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,EmpDepAdjust.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工部门调动信息表.xls","UTF-8"));
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
}
