package com.cy.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cy.enums.AttendanceStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author cy
 * @version 1.0
 */
@Data
@TableName("t_attendance")
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Excel(name = "员工编号",width = 15)
    @TableField(exist = false)
    private String workId;

    @Excel(name = "员工姓名",width = 15)
    @TableField(exist = false)
    private String name;

    @TableField("e_id")
    private Integer eId;

    @Excel(name = "考勤日期",width = 15)
    @ApiModelProperty("考勤日期")
    @TableField("attendance_date")
    private Date attendanceDate;

    @Excel(name = "上午打卡时间",width = 15)
    @TableField("mor_time")
    private String morTime;
//    @DateTimeFormat(pattern = "HH:mm:ss")
//    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
//    private Timestamp morTime;

    @Excel(name = "下午打卡时间",width = 15)
    @TableField("aft_time")
    private String aftTime;
//    @DateTimeFormat(pattern = "HH:mm:ss")
//    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
//    private Timestamp aftTime;


    @TableField("status")
    private AttendanceStatusEnum status;

    @Excel(name = "状态",replace = {"正常_0","迟到_1","早退_2","旷工_3","休息_4","请假_5"})
    @TableField(exist = false)
    private Integer statusFlag;
}
