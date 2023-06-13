package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.Menu;


import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IMenuService extends IService<Menu> {

    //通过用户ID查询菜单列表
    List<Menu> getMenusByAdminId();

    //根据角色查询菜单
    List<Menu> getMenusWithRole();

    //获取所有菜单
    List<Menu> getAllMenus();
}
