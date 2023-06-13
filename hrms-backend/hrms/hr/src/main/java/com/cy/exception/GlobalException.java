package com.cy.exception;

import com.cy.pojo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常
 * @author cy
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    public RespBean mySQLException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关数据，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public RespBean handle(ServiceException exception){
        log.error(exception.getMessage());
        return RespBean.error(exception.getMessage());
    }
}
