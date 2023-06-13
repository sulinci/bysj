package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.EmployeeTrainMapper;
import com.cy.pojo.Employee;
import com.cy.pojo.EmployeeTrain;
import com.cy.pojo.RespBean;
import com.cy.pojo.RespPageBean;
import com.cy.service.IEmployeeService;
import com.cy.service.IEmployeeTrainService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class EmployeeTrainServiceImpl extends ServiceImpl<EmployeeTrainMapper, EmployeeTrain> implements IEmployeeTrainService {

    @Autowired
    private EmployeeTrainMapper employeeTrainMapper;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询培训信息
     * @param currentPage
     * @param size
     * @param employeeTrain
     * @return
     */
    @Override
    public RespBean getTrainByPage(Integer currentPage, Integer size, EmployeeTrain employeeTrain) {
        Page<EmployeeTrain> page = new Page<>(currentPage, size);
        IPage<EmployeeTrain> employeeTrainIPage = employeeTrainMapper.getTrainByPage(page, employeeTrain);
        // TODO 返回培训信息
        RespPageBean respPageBean = new RespPageBean(employeeTrainIPage.getTotal(),employeeTrainIPage.getRecords());
        return RespBean.ok(respPageBean);
    }

    /**
     * 删除培训信息
     * @param ids
     * @return
     */
    @Override
    public RespBean deleteTrainByIds(List<Integer> ids) {
        LambdaQueryWrapper<EmployeeTrain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EmployeeTrain::getId,ids);
        boolean flag = this.removeByIds(ids);
        if (flag){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除删除，请稍后重试");
    }

    /**
     * 添加培训信息
     * @param employeeTrain
     */
    @Override
    public RespBean addTrain(EmployeeTrain employeeTrain) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getWorkId,employeeTrain.getEmpWorkID());
        queryWrapper.eq(Employee::getName,employeeTrain.getEmpName());
        Employee employee = employeeService.getOne(queryWrapper);
        if (employee == null){
            return RespBean.error("员工工号或姓名错误,请检查后输入");
        }
        employeeTrain.setEid(employee.getId());
        int i = employeeTrainMapper.insert(employeeTrain);
        if (i > 0){
            //TODO 发送邮件
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 修改培训信息
     * @param employeeTrain
     * @return
     */
    @Override
    public RespBean editTrain(EmployeeTrain employeeTrain) {
        int i = employeeTrainMapper.updateById(employeeTrain);
        if (i > 0){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败,请稍后重试");
    }

    /**
     * 导出培训信息
     * @param response
     * @param name
     */
    @Override
    public void export(HttpServletResponse response, String name) {
        List<EmployeeTrain> list = employeeTrainMapper.getEmployeeTrains(name);
        ExportParams params = new ExportParams("培训信息表","培训信息表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,EmployeeTrain.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("培训信息表.xls","UTF-8"));
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
