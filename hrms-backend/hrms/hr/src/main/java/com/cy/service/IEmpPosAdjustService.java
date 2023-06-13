package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.EmpPosAdjust;
import com.cy.pojo.RespBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IEmpPosAdjustService extends IService<EmpPosAdjust> {
    /**
     * 获取员工职位调动信息
     * @param currentPage
     * @param size
     * @param empPosAdjust
     * @return
     */
    RespBean getPosAdjustByPage(Integer currentPage, Integer size, EmpPosAdjust empPosAdjust);

    /**
     * 添加员工职位调动信息
     * @param empPosAdjust
     * @return
     */
    RespBean addPosAdjust(EmpPosAdjust empPosAdjust);

    /**
     * 删除员工职位调动信息
     * @param ids
     * @return
     */
    RespBean deletePosAdjustByIds(List<Integer> ids);

    /**
     * 修改员工职位调动信息
     * @param empPosAdjust
     * @return
     */
    RespBean editPosAdjust(EmpPosAdjust empPosAdjust);

    /**
     * 导出员工职位调动信息
     * @param response
     * @param name
     */
    void export(HttpServletResponse response, String name);
}
