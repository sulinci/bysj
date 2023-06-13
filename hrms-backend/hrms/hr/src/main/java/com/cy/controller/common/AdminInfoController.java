package com.cy.controller.common;

import com.cy.pojo.Admin;
import com.cy.pojo.RespBean;
import com.cy.service.IAdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author cy
 * @version 1.0
 */
@Slf4j
@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        //更新成功,重新构建Authentication对象
        if (adminService.updateById(admin)) {
            /**
             * 1.用户对象
             * 2.凭证（密码）
             * 3.用户角色
             */
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updatePassword(oldPass, pass, adminId);
    }
    @Value("${yebImage.path}")
    private String basePath;

    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "头像", dataType = "MultipartFile")})
    @PostMapping("/admin/userface")
    public RespBean updateUserFace(MultipartFile file, Integer id, Authentication authentication) {
        //获取文件上传地址
//        String[] uploadPath = FastDFSUtils.upload(file);
//        String url = FastDFSUtils.getTrackerUrl() + uploadPath[0] + "/" + uploadPath[1];
//        return adminService.updateAdminUserFace(url,id,authentication);

        log.info(file.toString());
        log.info("文件上传...");

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()){
            //目录不存在，则创建
            dir.mkdirs();
        }

        try {
            //将文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return adminService.updateAdminUserFace(fileName,id,authentication);
    }
}
