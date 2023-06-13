package com.cy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.pojo.Attendance;
import com.cy.pojo.vo.AttendanceVO;

import java.util.List;

/**
 * @author cy
 * @version 1.0
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    Attendance findByEmpId(Integer eId,String day);

    Integer countTimes(Integer eId,Integer status,String month);

    List<AttendanceVO> getAttendanceVOs(String name);
}
