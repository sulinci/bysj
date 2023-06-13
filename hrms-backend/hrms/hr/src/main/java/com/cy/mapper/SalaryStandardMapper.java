package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cy.pojo.SalaryStandard;
import com.cy.pojo.vo.SalaryStandardVO;

import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface SalaryStandardMapper extends BaseMapper<SalaryStandard> {
    IPage<SalaryStandardVO> getSalaryStandardByPage(IPage<SalaryStandardVO> config, String name);

    SalaryStandard getSalaryStandardByEId(Integer eId);

    List<SalaryStandardVO> getStandards(String name);
}
