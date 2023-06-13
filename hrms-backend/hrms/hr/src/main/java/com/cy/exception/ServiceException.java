package com.cy.exception;

import lombok.Getter;

/**
 * 自定义异常
 * @author
 * @date
 */
@Getter
public class ServiceException extends RuntimeException{

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
