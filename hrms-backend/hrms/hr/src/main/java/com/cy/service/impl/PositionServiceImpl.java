package com.cy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cy.mapper.PositionMapper;
import com.cy.pojo.Position;
import com.cy.service.IPositionService;
import org.springframework.stereotype.Service;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
