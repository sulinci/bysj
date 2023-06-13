package com.cy.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author cy
 * @version 1.0
 */
@Data
@TableName("t_salary_standard")
@ApiModel(value = "SalaryStandard对象", description = "薪资标准")
public class SalaryStandard implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工id")
    @TableField("e_id")
    private Integer eId;

    @ApiModelProperty("五险一金id")
    @TableField("soc_id")
    private Integer socId;

    @ApiModelProperty("基础工资")
    @TableField("base_salary")
    private BigDecimal baseSalary;

    @ApiModelProperty("生活补贴")
    @TableField("subsidy")
    private BigDecimal subsidy;

    @ApiModelProperty("奖金")
    @TableField("bonus")
    private BigDecimal bonus;

    @ApiModelProperty("公积金基数")
    @TableField("house_base")
    private BigDecimal houseBase;

    @ApiModelProperty("公积金个人缴纳费用")
    @TableField("per_house_pay")
    private BigDecimal perHousePay;

    @ApiModelProperty("公积金企业缴纳费用")
    @TableField("com_house_pay")
    private BigDecimal comHousePay;

    @ApiModelProperty("社保基数")
    @TableField("social_base")
    private BigDecimal socialBase;

    @ApiModelProperty("社保企业缴纳费用")
    @TableField("com_social_pay")
    private BigDecimal comSocialPay;

    @ApiModelProperty("社保个人缴纳费用")
    @TableField("per_social_pay")
    private BigDecimal perSocialPay;


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
