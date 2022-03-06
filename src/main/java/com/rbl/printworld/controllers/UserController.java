package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/access")
	public ResponseEntity<?> getAccess(@RequestParam("user") String userJson) {
		Gson gson = new Gson();
		User user = gson.fromJson(userJson, User.class);

		log.info("Check access for user : " + user.toString());

		boolean access = userService.getAccessLevelUser(user);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(access));
	}
}
