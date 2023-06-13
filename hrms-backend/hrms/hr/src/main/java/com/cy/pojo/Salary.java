package com.cy.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author cy
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_salary")
@ApiModel(value="Salary对象", description="")
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工id")
    @TableField("e_id")
    private Integer eId;

    @ApiModelProperty(value = "员工编号")
    @Excel(name = "员工编号",width = 15)
    @TableField(exist = false)
    private String empWorkId;

    @ApiModelProperty(value = "员工姓名")
    @Excel(name = "员工姓名",width = 15)
    @TableField(exist = false)
    private String empName;

    @ApiModelProperty(value = "所属部门")
    @Excel(name = "部门",width = 15)
    @TableField(exist = false)
    private String depName;

    @ApiModelProperty(value = "月份")
    @Excel(name = "月份",width = 15)
    private String month;

    @ApiModelProperty(value = "基本工资")
    @Excel(name = "基本工资",width = 15)
    @TableField("basic_salary")
    private BigDecimal basicSalary;

    @ApiModelProperty(value = "生活补贴")
    @Excel(name = "生活补贴",width = 15)
    private BigDecimal subsidy;

    @ApiModelProperty(value = "奖金")
    @Excel(name = "奖金",width = 15)
    private BigDecimal bonus;

    @ApiModelProperty(value = "迟到扣款")
    @Excel(name = "迟到扣款",width = 15)
    @TableField("late_deduct")
    private BigDecimal lateDeduct;

    @ApiModelProperty(value = "早退扣款")
    @Excel(name = "早退扣款",width = 15)
    @TableField("leave_early_deduct")
    private BigDecimal leaveEarlyDeduct;

    @ApiModelProperty(value = "旷工扣款")
    @Excel(name = "旷工扣款",width = 15)
    @TableField("absenteeism_deduct")
    private BigDecimal absenteeismDeduct;

    @ApiModelProperty(value = "请假扣款")
    @Excel(name = "请假扣款",width = 15)
    @TableField("leave_deduct")
    private BigDecimal leaveDeduct;

    @ApiModelProperty(value = "公积金个人缴纳费用")
    @Excel(name = "公积金个人缴纳费用",width = 15)
    @TableField("per_house_pay")
    private BigDecimal perHousePay;

    @ApiModelProperty(value = "公积金企业缴纳费用")
    @TableField("com_house_pay")
    private BigDecimal comHousePay;

    @ApiModelProperty(value = "社保个人缴纳费用")
    @Excel(name = "社保个人缴纳费用",width = 15)
    @TableField("per_social_pay")
    private BigDecimal perSocialPay;

    @ApiModelProperty(value = "社保企业缴纳费用")
    @TableField("com_social_pay")
    private BigDecimal comSocialPay;

    @ApiModelProperty(value = "应发工资")
    @Excel(name = "应发工资",width = 15)
    @TableField("all_salary")
    private BigDecimal allSalary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Timestamp updateTime;

    @ApiModelProperty("逻辑删除，0未删除，1删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer deleteFlag;

}
