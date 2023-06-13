package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.pojo.EmpDepAdjust;
import com.cy.pojo.EmpPosAdjust;

import java.util.List;


/**
 * @author cy
 * @version 1.0
 */
public interface EmpDepAdjustMapper extends BaseMapper<EmpDepAdjust> {

    IPage<EmpDepAdjust> getDepAdjustByPage(Page<EmpDepAdjust> page, EmpDepAdjust empDepAdjust);

    List<EmpDepAdjust> getEmpDepAdjusts(String name);
}
