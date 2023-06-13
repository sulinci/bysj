package com.cy.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author cy
 * @version 1.0
 */
@Data
@TableName("t_emp_dep_adjust")
public class EmpDepAdjust  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工id")
    @TableField("e_id")
    private Integer eid;

    @Excel(name = "员工编号",width = 15)
    @ApiModelProperty(value = "员工编号")
    @TableField(exist = false)
    private String empWorkID;

    @Excel(name = "员工姓名",width = 15)
    @ApiModelProperty(value = "员工姓名")
    @TableField(exist = false)
    private String empName;

    @ApiModelProperty(value = "调动前部门id")
    @TableField("before_dep_id")
    private Integer beforeDepId;


    @ApiModelProperty(value = "调动后部门id")
    @TableField("dep_id")
    private Integer afterDepId;

    @Excel(name = "调动前部门",width = 15)
    @ApiModelProperty(value = "调动前部门")
    @TableField(exist = false)
    private String beforeDep;

    @Excel(name = "调动后部门",width = 15)
    @ApiModelProperty(value = "调动后部门")
    @TableField(exist = false)
    private String afterDep;

    @Excel(name = "调动日期",width = 15)
    @ApiModelProperty(value = "调动日期")
    @TableField("adjust_date")
    private LocalDate adjustDate;

    @Excel(name = "调动原因",width = 25)
    @ApiModelProperty(value = "调动原因")
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Timestamp updateTime;

    @ApiModelProperty("逻辑删除，0未删除，1删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer deleteFlag;
}
