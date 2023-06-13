package com.cy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.pojo.EmployeeTrain;

import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface EmployeeTrainMapper extends BaseMapper<EmployeeTrain> {

    IPage<EmployeeTrain> getTrainByPage(Page<EmployeeTrain> page, EmployeeTrain employeeTrain);

    List<EmployeeTrain> getEmployeeTrains(String name);
}
