package com.cy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cy.mapper.RoleMapper;
import com.cy.pojo.Role;
import com.cy.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
