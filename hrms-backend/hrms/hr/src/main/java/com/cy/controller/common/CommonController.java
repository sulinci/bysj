package com.cy.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.pojo.RespBean;
import com.cy.pojo.SocDep;
import com.cy.service.IDepartmentService;
import com.cy.service.IEmployeeService;
import com.cy.service.ISocDepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author cy
 * @version 1.0
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Resource
    private IEmployeeService employeeService;

    @Resource
    private ISocDepService socDepService;

    @Resource
    private IDepartmentService departmentService;

    @Value("${yebImage.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public RespBean upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

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

        return RespBean.success(fileName);

    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器显示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计今日员工各状态人数
     * @return
     */
    @GetMapping("/count")
    public RespBean getCountData(){
        return employeeService.getCountData();
    }

    /**
     * 获取各部门五险一金比例信息
     * @return
     */
    @GetMapping("/soc")
    public RespBean getSocData(){
        List<SocDep> list = socDepService.list(new QueryWrapper<SocDep>().last("limit 5"));
        return RespBean.ok(list);
    }

    /**
     * 统计各季度入职人数
     * @return
     */
    @GetMapping("/emp")
    public RespBean getEmpData(){
        return employeeService.getEmpData();
    }

    /**
     * 获取各部门人数信息
     * @return
     */
    @GetMapping("/dep")
    public RespBean getDepartmentData(){
        return departmentService.getDepartmentData();
    }
}
