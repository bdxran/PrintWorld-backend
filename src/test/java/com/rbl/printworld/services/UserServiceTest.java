package com.rbl.printworld.services;

import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import({UserServiceImpl.class})
@Slf4j
public class UserServiceTest {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Test
	public void getAccessLevelUserTest(){
		boolean allow = false;
		User user = new User("rbl", "rbl@test.com", "test", Access.USER);

		allow = userServiceImpl.getAccessLevelUser(user);

		Assert.assertNotNull(allow);
		Assert.assertTrue("User has user access", allow);
	}
}
