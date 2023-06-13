package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.MenuRole;
import com.cy.pojo.RespBean;

/**
 * @author cy
 * @version 1.0
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);

}
