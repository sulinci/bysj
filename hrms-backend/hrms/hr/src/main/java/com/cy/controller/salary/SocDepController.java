package com.cy.controller.salary;

import com.cy.pojo.RespBean;
import com.cy.pojo.SocDep;
import com.cy.service.ISocDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cy
 * @version 1.0
 */
@RestController
@RequestMapping("/salary/soc-dep")
public class SocDepController {

    @Autowired
    private ISocDepService socDepService;

    /**
     * 获取五险一金比例信息
     * @param currentPage
     * @param size
     * @param socDep
     * @return
     */
    @GetMapping("/")
    public RespBean getSocDepByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      SocDep socDep){
        return socDepService.getSocDepByPage(currentPage,size,socDep);
    }

    /**
     * 删除五险一金信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespBean deleteSocDepById(@PathVariable Integer id){
        return socDepService.deleteSocDepById(id);
    }

    /**
     * 更新五险一金信息
     * @param socDep
     * @return
     */
    @PutMapping("/")
    public RespBean editSocDep(@RequestBody SocDep socDep){
        return socDepService.editSocDep(socDep);
    }

    /**
     * 新增五险一金信息
     * @param socDep
     * @return
     */
    @PostMapping("/")
    public RespBean addSocDep(@RequestBody SocDep socDep){
        return socDepService.addSocDep(socDep);
    }
}
