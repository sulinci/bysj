package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * @author cy
 * @version 1.0
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    Integer insertRecord(@Param("rid")Integer rid,@Param("mids") Integer[] mids);

}
