package com.cy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.pojo.Department;
import com.cy.pojo.RespBean;


import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @return
     */
    RespBean getAllDepartments();

    /**
     * 添加部门
     * @param dep
     * @return
     */
    RespBean addDep(Department dep);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RespBean deleteDep(Integer id);

    /**
     * 编辑部门
     * @param department
     * @return
     */
    RespBean editDep(Department department);

    RespBean getDepartmentData();

}
