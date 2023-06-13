package com.cy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cy.mapper.MenuRoleMapper;
import com.cy.pojo.MenuRole;
import com.cy.pojo.RespBean;
import com.cy.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        Set<String> keys = redisTemplate.keys("menu:userId:*");
        if (mids == null || mids.length==0){
            redisTemplate.delete(keys);
            return RespBean.success("更新成功");
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if (mids.length == result){
            redisTemplate.delete(keys);
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");

    }
}
