package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.EmpPosAdjustMapper;
import com.cy.pojo.*;
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
public class EmpPosAdjustServiceImpl extends ServiceImpl<EmpPosAdjustMapper, EmpPosAdjust> implements IEmpPosAdjustService {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private EmpPosAdjustMapper empPosAdjustMapper;

    /**
     * 获取员工职位调动信息
     * @param currentPage
     * @param size
     * @param empPosAdjust
     * @return
     */
    @Override
    public RespBean getPosAdjustByPage(Integer currentPage, Integer size, EmpPosAdjust empPosAdjust) {
        Page<EmpPosAdjust> page = new Page<>(currentPage, size);
        IPage<EmpPosAdjust> posAdjustIPage = empPosAdjustMapper.getPosAdjustByPage(page, empPosAdjust);
        System.out.println("posAdjustIPage = " + posAdjustIPage.getRecords());
        RespPageBean respPageBean = new RespPageBean(posAdjustIPage.getTotal(),posAdjustIPage.getRecords());
        return RespBean.ok(respPageBean);
    }

    /**
     * 删除员工职位调动信息
     * @param ids
     * @return
     */
    @Override
    public RespBean deletePosAdjustByIds(List<Integer> ids) {
        LambdaQueryWrapper<EmpPosAdjust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EmpPosAdjust::getId,ids);
        boolean flag = this.removeByIds(ids);
        if (flag){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除删除，请稍后重试");
    }

    /**
     * 添加员工职位调动信息
     * @param empPosAdjust
     * @return
     */
    @Override
    @Transactional
    public RespBean addPosAdjust(EmpPosAdjust empPosAdjust) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getWorkId,empPosAdjust.getEmpWorkID());
        queryWrapper.eq(Employee::getName,empPosAdjust.getEmpName());
        Employee employee = employeeService.getOne(queryWrapper);
        if (employee == null){
            return RespBean.error("员工工号或姓名错误,请检查后输入");
        }
        if (employee.getPosId() == empPosAdjust.getAfterPosId()){
            return RespBean.error("员工已是该职位,请检查后输入");
        }
        empPosAdjust.setEid(employee.getId());
        empPosAdjust.setBeforePosId(employee.getPosId());
        int i = empPosAdjustMapper.insert(empPosAdjust);
        if (i > 0){
            //TODO 修改员工职位部门
            employee.setPosId(empPosAdjust.getAfterPosId());
            employeeService.updateById(employee);
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 修改员工职位调动信息
     * @param empPosAdjust
     * @return
     */
    @Override
    public RespBean editPosAdjust(EmpPosAdjust empPosAdjust) {
        int i = empPosAdjustMapper.updateById(empPosAdjust);
        if (i > 0){
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getWorkId,empPosAdjust.getEmpWorkID());
            queryWrapper.eq(Employee::getName,empPosAdjust.getEmpName());
            Employee employee = employeeService.getOne(queryWrapper);
            //判断员工职位是否被修改
            if (employee.getPosId() != empPosAdjust.getAfterPosId()){
                employee.setPosId(empPosAdjust.getAfterPosId());
                employeeService.updateById(employee);
            }
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    /**
     * 导出员工职位调动信息
     * @param response
     * @param name
     */
    @Override
    public void export(HttpServletResponse response, String name) {
        List<EmpPosAdjust> list = empPosAdjustMapper.getEmpPosAdjusts(name);
        ExportParams params = new ExportParams("员工职位调动信息表","员工职位调动信息表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,EmpPosAdjust.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工职位调动信息表.xls","UTF-8"));
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
