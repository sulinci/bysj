package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.EmployeeTrain;
import com.cy.pojo.RespBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IEmployeeTrainService extends IService<EmployeeTrain> {

    /**
     * 获取培训信息
     * @param currentPage
     * @param size
     * @param employeeTrain
     * @return
     */
    RespBean getTrainByPage(Integer currentPage, Integer size, EmployeeTrain employeeTrain);

    /**
     * 删除培训信息
     * @param ids
     * @return
     */
    RespBean deleteTrainByIds(List<Integer> ids);

    /**
     * 添加培训信息
     * @param employeeTrain
     */
    RespBean addTrain(EmployeeTrain employeeTrain);

    /**
     * 修改培训信息
     * @param employeeTrain
     */
    RespBean editTrain(EmployeeTrain employeeTrain);

    /**
     * 导出培训信息
     * @param response
     * @param name
     */
    void export(HttpServletResponse response, String name);
}
