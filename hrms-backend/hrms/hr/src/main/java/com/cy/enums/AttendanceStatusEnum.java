package com.cy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考勤状态枚举
 * @author cy
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum AttendanceStatusEnum implements BaseEnum {

    NORMAL(0, "正常"),
    LATE(1, "迟到"),
    LEAVE_EARLY(2, "早退"),
    ABSENTEEISM(3, "旷工"),
    REST(4, "休息"),
    LEAVE(5, "请假");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String message;
}