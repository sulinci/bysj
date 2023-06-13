package com.cy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.DepartmentMapper;
import com.cy.pojo.Department;
import com.cy.pojo.Employee;
import com.cy.pojo.RespBean;
import com.cy.service.IDepartmentService;
import com.cy.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Resource
    private IDepartmentService departmentService;

    @Resource
    private IEmployeeService employeeService;

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public RespBean getAllDepartments() {
        List<Department> list = list();
        return RespBean.ok(setSubDept(list));
    }

    /**
     * 为父级部门设置子部门，使用流来处理数据，并返回父级部门
     *
     * @param list
     * @return
     */
    public List<Department> setSubDept(List<Department> list) {
        // 父级菜单
        List<Department> parentList = list.stream().parallel()
                .filter(dept -> dept.getParentId() == 0).collect(Collectors.toList());
        for (Department parentDept : parentList) {
            // 子菜单
            List<Department> subList = list.stream().parallel()
                    .filter(dept -> dept.getParentId() == parentDept.getId()).collect(Collectors.toList());
            parentDept.setChildren(subList);
        }
        return parentList;
    }

    /**
     * 添加部门
     * @param dep
     * @return
     */
    @Override
    public RespBean addDep(Department dep) {
        int insert = departmentMapper.insert(dep);
        if (insert > 0){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDep(Integer id) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getParentId,id);
        Integer integer = departmentMapper.selectCount(queryWrapper);
        if (integer == 0){
            int i = departmentMapper.deleteById(id);
            if (i > 0){
                return RespBean.success("删除成功");
            }
            return RespBean.error("删除失败");
        }
        return RespBean.error("删除失败,该部门有子部门");
    }

    /**
     * 编辑部门
     * @param department
     * @return
     */
    @Override
    public RespBean editDep(Department department) {
        int i = departmentMapper.updateById(department);
        if (i > 0){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @Override
    public RespBean getDepartmentData() {
        List<Department> parentList = this.departmentService.list(new QueryWrapper<Department>().eq("parent_id", 0));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Department parentDept : parentList) {
            List<Department> list = this.departmentService.list(new QueryWrapper<Department>().eq("parent_id", parentDept.getId()));
            List<Integer> ids = list.stream().map(Department::getId).collect(Collectors.toList());
            long num = this.employeeService.count(new QueryWrapper<Employee>().in("dep_id", ids));
            Map<String, Object> map = new HashMap<>();
            map.put("value", num);
            map.put("name", parentDept.getName());
            mapList.add(map);
        }
        return RespBean.ok(mapList);
    }
}
