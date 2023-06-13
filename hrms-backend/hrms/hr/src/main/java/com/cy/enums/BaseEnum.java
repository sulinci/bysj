package com.cy.enums;

/**
 * 枚举基类
 * @author cy
 * @version 1.0
 */
public interface BaseEnum {
    /**
     * 获取编号
     * @return
     */
    Integer getCode();

    /**
     * 消息内容
     * @return
     */
    String getMessage();
}