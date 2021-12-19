package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.ModelService;
import com.rbl.printworld.services.ToolService;
import com.rbl.printworld.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/api/model")
public class ModelController {

	private final User user = new User("rbl", "rbl@test.com", "test", Access.USER);

	private final ToolService toolService;
	private final UserService userService;
	private final ModelService modelService;

	@Autowired
	public ModelController(ToolService toolService,
	                       UserService userService, ModelService modelService) {
		this.toolService = toolService;
		this.userService = userService;
		this.modelService = modelService;
	}

	/**
	 * Web Service to create new model
	 *
	 * @return a ResponseEntity
	 * @RequestParam multipartFile
	 * @RequestBody model
	 */
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<?> createModel(@RequestParam("file") MultipartFile multipartFile, @RequestParam("model") String modelJson) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		log.info("Call to create new model");
		Gson gson = new Gson();
		Model model = gson.fromJson(modelJson, Model.class);

		toolService.getExtensionFile(model);
		if (!model.getExtension().equals("zip")) {
			log.warn("File to send isn't zip, is : " + model.getExtension());
			throw new ApplicationException("415", "File upload isn't zip!");
		}

		String id = toolService.generateId();
		String pathFileTmp = toolService.transferMultipartFileToFile(multipartFile, id);
		Model modelSave = modelService.createModel(id, pathFileTmp, model);

		log.info("New model is save and upload");

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(modelSave));
	}

	/**
	 * Web Service to modify model
	 *
	 * @param model
	 * @return a ResponseEntity
	 */
	@PostMapping("/modify")
	public ResponseEntity<Object> modifyModel(@RequestBody Model model) {
		//TODO
		return null;
	}

	/**
	 * Web Service to delete model
	 *
	 * @param model
	 * @return a ResponseEntity
	 */
	@PostMapping("/delete")
	public ResponseEntity<Object> deleteModel(@RequestBody Model model) {
		//TODO
		return null;
	}
}
