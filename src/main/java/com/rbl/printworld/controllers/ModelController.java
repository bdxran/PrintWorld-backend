package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.User;
import com.rbl.printworld.models.dto.ListResponseDto;
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
	 * Web Service to get model by id
	 *
	 * @return a ResponseEntity
	 * @Param String id
	 */
	@GetMapping("/byid/{id}")
	public ResponseEntity<?> getModelById(@PathVariable String id) {
		Model model = modelService.getModelById(id);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(model));
	}

	/**
	 * Web Service to get all model
	 *
	 * @return a ResponseEntity
	 * @Param Integer page but not required
	 * @Param Integer limit but not required
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllModel(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
		ListResponseDto<Model> models = modelService.getAllModel(page, limit);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(models));
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

		toolService.getExtensionFile(model, multipartFile.getOriginalFilename());
		if (!model.getExtension().equals("zip")) {
			log.warn("File to send isn't zip, is : " + model.getExtension());
			throw new ApplicationException("415", "File upload isn't zip!");
		}

		String id = toolService.generateId();
		String pathFileTmp = toolService.transferMultipartFileToFile(multipartFile, id);
		Model modelSave = modelService.createModel(id, pathFileTmp, multipartFile.getOriginalFilename(), model);

		log.info("New model is save");

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(modelSave));
	}

	/**
	 * Web Service to modify model
	 *
	 * @return a ResponseEntity
	 * @RequestParam multipartFile
	 * @RequestBody model
	 */
	@PostMapping(value = "/modify", consumes = "multipart/form-data")
	public ResponseEntity<Object> modifyModel(@RequestParam("file") MultipartFile multipartFile, @RequestParam("model") String modelJson) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		log.info("Call to update model");
		Gson gson = new Gson();
		Model model = gson.fromJson(modelJson, Model.class);

		toolService.getExtensionFile(model, multipartFile.getOriginalFilename());
		if (!model.getExtension().equals("zip")) {
			log.warn("File to send isn't zip, is : " + model.getExtension());
			throw new ApplicationException("415", "File upload isn't zip!");
		}

		String pathFileTmp = toolService.transferMultipartFileToFile(multipartFile, model.getId());
		Model modelSave = modelService.modifyModel(pathFileTmp, model);

		log.info("Model is upload");

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(modelSave));
	}

	/**
	 * Web Service to delete model
	 *
	 * @return a ResponseEntity
	 * @RequestBody model
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteModel(@RequestBody Model model) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		log.info("Call to delete model");

		if (!modelService.deleteModel(model)) {
			log.error("Model with id " + model.getId() + " isn't delete");
			throw new ApplicationException("500", "Model with id " + model.getId() + " isn't delete");
		}

		log.info("Model and file were delete");

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body("Model with id " + model.getId() + " is to delete");
	}
}
