package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.pojo.EmpPosAdjust;

import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface EmpPosAdjustMapper extends BaseMapper<EmpPosAdjust> {

    IPage<EmpPosAdjust> getPosAdjustByPage(Page<EmpPosAdjust> page, EmpPosAdjust empPosAdjust);

    List<EmpPosAdjust> getEmpPosAdjusts(String name);
}
