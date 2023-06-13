package com.cy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.mapper.SocDepMapper;
import com.cy.pojo.RespBean;
import com.cy.pojo.SocDep;
import com.cy.service.ISocDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cy
 * @version 1.0
 */
@Service
public class SocDepServiceImpl extends ServiceImpl<SocDepMapper,SocDep> implements ISocDepService {

    @Autowired
    private SocDepMapper socDepMapper;


    /**
     * 获取五险一金比例信息
     * @param currentPage
     * @param size
     * @param socDep
     * @return
     */
    @Override
    public RespBean getSocDepByPage(Integer currentPage, Integer size, SocDep socDep) {
        Page<SocDep> page = new Page<>(currentPage,size);
        Page<SocDep> socDepPage = socDepMapper.selectPage(page, null);
        return RespBean.ok(socDepPage);
    }

    /**
     * 删除五险一金信息根据id
     * @param id
     * @return
     */
    @Override
    public RespBean deleteSocDepById(Integer id) {
        int i = socDepMapper.deleteById(id);
        if (i > 0){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    /**
     * 更新五险一金信息
     * @param socDep
     * @return
     */
    @Override
    public RespBean editSocDep(SocDep socDep) {
        int i = socDepMapper.updateById(socDep);
        if (i > 0){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    /**
     * 新增五险一金信息
     * @param socDep
     * @return
     */
    @Override
    public RespBean addSocDep(SocDep socDep) {
        int i = socDepMapper.insert(socDep);
        if (i > 0){
            return RespBean.success("新增成功");
        }
        return RespBean.error("新增失败");
    }
}
