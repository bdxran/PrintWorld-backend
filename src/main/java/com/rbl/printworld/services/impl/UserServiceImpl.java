package com.rbl.printworld.services.impl;

import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	public boolean getAccessLevelUser(User user) {
		if (user.getAccess().equals(Access.USER) || user.getAccess().equals(Access.ADMIN))
			return true;
		else {
			return false;
		}
	}
}
