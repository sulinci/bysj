package com.cy.pojo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cy
 * @version 1.0
 */
@Data
public class SalaryStandardVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "员工编号",width = 15)
    @ApiModelProperty(value = "工号")
    private String workId;

    @ApiModelProperty("员工id")
    private Integer eId;

    @Excel(name = "员工姓名",width = 15)
    @ApiModelProperty("员工姓名")
    private String name;

    @Excel(name = "部门",width = 15)
    @ApiModelProperty(value = "所属部门")
    private String depName;

    @ApiModelProperty("五险一金id")
    private Integer socId;

    @Excel(name = "五险一金账套",width = 15)
    @ApiModelProperty("五险一金名字")
    private String socName;

    @ApiModelProperty("薪资标准id")
    private Integer standId;

    @Excel(name = "基础工资",width = 15)
    @ApiModelProperty("基础工资")
    private BigDecimal baseSalary;

    @Excel(name = "生活补贴",width = 15)
    @ApiModelProperty("生活补贴")
    private BigDecimal subsidy;

    @Excel(name = "奖金",width = 15)
    @ApiModelProperty("奖金")
    private BigDecimal bonus;

    @Excel(name = "公积金基数",width = 15)
    @ApiModelProperty("公积金基数")
    private BigDecimal houseBase;

    @Excel(name = "社保基数",width = 15)
    @ApiModelProperty("社保基数")
    private BigDecimal socialBase;

}
