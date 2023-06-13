package com.cy.controller.common;

import com.cy.pojo.Admin;
import com.cy.pojo.AdminLogin;
import com.cy.pojo.RespBean;
import com.cy.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 * @author cy
 * @version 1.0
 */
@Slf4j
@RestController
public class AdminLoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request){
        return adminService.login(adminLogin.getUsername(),adminLogin.getPassword(),adminLogin.getCode(),request);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/admin/info")
    public RespBean getAdminInfo(Principal principal){
        if (principal == null){
            return null;
        }
        System.out.println("principal = " + principal);
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        //System.out.println("admin = " + admin);
        admin.setPassword(null);
        admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
//        log.error("admin/info使用");
//        log.error("principal = {}",principal);
        return RespBean.ok(admin);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }
}
