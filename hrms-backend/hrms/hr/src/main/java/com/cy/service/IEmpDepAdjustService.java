package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.EmpDepAdjust;
import com.cy.pojo.EmpPosAdjust;
import com.cy.pojo.RespBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IEmpDepAdjustService extends IService<EmpDepAdjust> {
    /**
     * 获取员工部门调动信息
     * @param currentPage
     * @param size
     * @param empDepAdjust
     * @return
     */
    RespBean getDepAdjustByPage(Integer currentPage, Integer size, EmpDepAdjust empDepAdjust);

    /**
     * 添加员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    RespBean addDepAdjust(EmpDepAdjust empDepAdjust);

    /**
     * 删除员工部门调动信息
     * @param ids
     * @return
     */
    RespBean deleteDepAdjustByIds(List<Integer> ids);

    /**
     * 修改员工部门调动信息
     * @param empDepAdjust
     * @return
     */
    RespBean editDepAdjust(EmpDepAdjust empDepAdjust);

    /**
     * 导出员工部门调动信息
     * @param response
     * @param name
     */
    void export(HttpServletResponse response, String name);
}
