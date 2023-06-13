package com.cy.pojo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cy
 * @version 1.0
 */
@Data
public class AttendanceVO {

    private Integer id;

    @Excel(name = "员工编号",width = 15)
    private String workId;

    @Excel(name = "员工姓名",width = 15)
    private String name;

    @Excel(name = "部门",width = 15)
    private String depName;

    @Excel(name = "迟到次数",width = 10)
    private Integer lateCount;

    @Excel(name = "早退次数",width = 10)
    private Integer leaveEarlyCount;

    @Excel(name = "旷工次数",width = 10)
    private Integer absenteeismCount;

    @Excel(name = "请假天数",width = 10)
    private Integer leaveCount;
}
