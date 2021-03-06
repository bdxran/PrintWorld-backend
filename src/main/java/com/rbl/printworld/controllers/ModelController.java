package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Image;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.User;
import com.rbl.printworld.models.dto.ListResponseDto;
import com.rbl.printworld.services.ImageService;
import com.rbl.printworld.services.ModelService;
import com.rbl.printworld.services.ToolService;
import com.rbl.printworld.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping(value = "/api/model")
public class ModelController {

	private final User user = new User("rbl", "rbl@test.com", "test", Access.USER);

	private final ToolService toolService;
	private final UserService userService;
	private final ModelService modelService;
	private final ImageService imageService;

	@Autowired
	public ModelController(ToolService toolService, UserService userService,
	                       ModelService modelService, ImageService imageService) {
		this.toolService = toolService;
		this.userService = userService;
		this.modelService = modelService;
		this.imageService = imageService;
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
	public ResponseEntity<?> createModel(@RequestParam("file") MultipartFile file, @RequestParam("images") String[] images, @RequestParam("model") String modelJson) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		Model modelSave = modelService.createModel(file, images, modelJson);

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
	public ResponseEntity<Object> modifyModel(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
	                                          @RequestParam(name = "images", required = false) String[] images,
	                                          @RequestParam("model") String modelJson) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		Model modelSave = modelService.modifyModel(multipartFile, images, modelJson);

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
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteModel(@PathVariable String id) {
		if (!userService.getAccessLevelUser(user)) {
			throw new ApplicationException("403", "Bad access, level USER");
		}

		log.info("Call to delete model for id : " + id);
		Model model = modelService.getModelById(id);

		for(String idImage : model.getImageIds()) {
			Image image = imageService.getImageById(idImage);
			imageService.deleteImage(image);
		}

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

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadModel(@PathVariable("id") String id) {
		Resource file = modelService.downloadModel(id);

		try {
			Path path = file.getFile().toPath();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
							+ file.getFilename() + "\"")
					.body(file);
		} catch (IOException ex) {
			throw new ApplicationException("500", "Error to recover image");
		}
	}
}
