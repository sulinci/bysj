package com.cy.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@TableName("t_soc_dep")
@ApiModel(value = "SocDep对象", description = "五险一金")
public class SocDep implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("参保部门")
    @TableField("name")
    private String name;

    @ApiModelProperty("养老保险个人缴费比例")
    @TableField("per_pension_rate")
    private BigDecimal perPensionRate;


    @ApiModelProperty("养老保险企业缴费比例")
    @TableField("com_pension_rate")
    private BigDecimal comPensionRate;


    @ApiModelProperty("医疗保险个人缴费比例")
    @TableField("per_medical_rate")
    private BigDecimal perMedicalRate;


    @ApiModelProperty("医疗保险企业缴费比例")
    @TableField("com_medical_rate")
    private BigDecimal comMedicalRate;


    @ApiModelProperty("失业保险个人缴费比例")
    @TableField("per_unemployment_rate")
    private BigDecimal perUnemploymentRate;


    @ApiModelProperty("失业保险企业缴费比例")
    @TableField("com_unemployment_rate")
    private BigDecimal comUnemploymentRate;


    @ApiModelProperty("生育保险企业缴费比例")
    @TableField("com_maternity_rate")
    private BigDecimal comMaternityRate;

    @ApiModelProperty("工伤保险企业缴费比例")
    @TableField("com_injury_rate")
    private BigDecimal comInjuryRate;

    @ApiModelProperty("公积金个人缴费比例")
    @TableField("per_house_rate")
    private BigDecimal perHouseRate;

    @ApiModelProperty("公积金企业缴费比例")
    @TableField("com_house_rate")
    private BigDecimal comHouseRate;

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