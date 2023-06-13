package com.cy;

import com.cy.mapper.AdminMapper;
import com.cy.pojo.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author cy
 * @version 1.0
 */
@SpringBootTest
public class Test1 {

    @Autowired
    private AdminMapper adminMapper;

    @Test
    void password(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Admin admin = new Admin();
        admin.setId(5);
        String pass = "123";
        admin.setEnabled(false);
        admin.setPassword(encoder.encode(pass));
        int i = adminMapper.updateById(admin);
    }

}
