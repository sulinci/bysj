package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.RespBean;
import com.cy.pojo.SocDep;

/**
 * @author cy
 * @version 1.0
 */
public interface ISocDepService extends IService<SocDep> {
    /**
     * 获取五险一金比例信息
     * @param currentPage
     * @param size
     * @param socDep
     * @return
     */
    RespBean getSocDepByPage(Integer currentPage, Integer size, SocDep socDep);

    /**
     * 删除五险一金信息
     * @param id
     * @return
     */
    RespBean deleteSocDepById(Integer id);

    /**
     * 更新五险一金信息
     * @param socDep
     * @return
     */
    RespBean editSocDep(SocDep socDep);

    /**
     * 新增五险一金信息
     * @param socDep
     * @return
     */
    RespBean addSocDep(SocDep socDep);
}
