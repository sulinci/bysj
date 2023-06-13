package com.cy.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.SalaryStandardMapper;
import com.cy.mapper.SocDepMapper;
import com.cy.pojo.Employee;
import com.cy.pojo.RespBean;
import com.cy.pojo.SalaryStandard;
import com.cy.pojo.SocDep;
import com.cy.pojo.vo.SalaryStandardVO;
import com.cy.service.ISalaryStandardService;
import com.cy.service.ISocDepService;
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
public class SalaryStandardServiceImpl extends ServiceImpl<SalaryStandardMapper,SalaryStandard> implements ISalaryStandardService {

    @Autowired
    private SalaryStandardMapper salaryStandardMapper;

    @Autowired
    private ISocDepService socDepService;

    /**
     * 分页查询薪资标准
     * @param currentPage
     * @param size
     * @param name
     * @return
     */
    @Override
    public RespBean getSalaryStandardByPage(Integer currentPage, Integer size, String name) {
        IPage<SalaryStandardVO> config = new Page<>(currentPage, size);
        IPage<SalaryStandardVO> page = this.salaryStandardMapper.getSalaryStandardByPage(config, name);
        return RespBean.ok(page);
    }

    /**
     * 设置薪资标准
     * @param salaryStandard
     * @return
     */
    @Override
    public RespBean setSalaryStandard(SalaryStandard salaryStandard) {
        LambdaQueryWrapper<SalaryStandard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalaryStandard::getEId,salaryStandard.getEId());
        if (saveOrUpdate(salaryStandard, queryWrapper)){
            return RespBean.success("设置成功");
        }
        return RespBean.error("设置失败");
    }

    /**
     * 获取五险一金信息
     * @return
     */
    @Override
    public RespBean getSoc() {
        List<SocDep> list = socDepService.list();
        return RespBean.ok(list);
    }

    @Override
    public void exportSalaryStandard(HttpServletResponse response, String name) {
        List<SalaryStandardVO> list = salaryStandardMapper.getStandards(name);
        ExportParams params = new ExportParams("员工薪资标准表","员工薪资标准表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,SalaryStandardVO.class,list);
        ServletOutputStream outputStream = null;
        try {
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工薪资标准表.xls","UTF-8"));
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
