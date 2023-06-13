package com.cy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cy.pojo.Salary;

import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface SalaryMapper extends BaseMapper<Salary> {

    IPage<Salary> getSalaryTable(IPage<Salary> config,String name, String month);

    List<Salary> getSalaryByMonth(String month);
}
