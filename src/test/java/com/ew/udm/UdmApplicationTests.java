package com.ew.udm;

import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserMapper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UdmApplicationTests {

    @Autowired
    UserMapper userMapper;
	@Test
	public void contextLoads() {
        User user = userMapper.selectByPrimaryKey(1);
        UserWithRole userWithRole = userMapper.selectUserRoleCollection("aad");
        System.out.println(userWithRole);

        List<User> users = userMapper.selectAll(1, 10);
        PageInfo page = new PageInfo(users);
        System.out.println(page);

	}

}
