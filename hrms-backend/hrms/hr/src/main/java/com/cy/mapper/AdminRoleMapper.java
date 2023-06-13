package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * @author cy
 * @version 1.0
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 添加操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer addRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);

}
