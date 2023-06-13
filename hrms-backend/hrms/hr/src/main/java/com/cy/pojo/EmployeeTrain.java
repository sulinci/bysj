package com.cy.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author cy
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee_train")
@ApiModel(value="EmployeeTrain对象", description="")
public class EmployeeTrain implements Serializable {

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

    @Excel(name = "培训日期",width = 15)
    @ApiModelProperty(value = "培训日期")
    @TableField("train_date")
    private LocalDate trainDate;

    @Excel(name = "结束日期",width = 15)
    @ApiModelProperty(value = "结束日期")
    @TableField("end_date")
    private LocalDate endDate;

    @Excel(name = "培训内容",width = 40)
    @ApiModelProperty(value = "培训内容")
    @TableField("train_content")
    private String trainContent;

    @Excel(name = "备注",width = 30)
    @ApiModelProperty(value = "备注")
    private String remark;

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
