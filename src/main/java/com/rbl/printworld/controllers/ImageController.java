package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Image;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

	private final User user = new User("rbl", "rbl@test.com", "test", Access.USER);

	private final ImageService imageService;

	@Autowired
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@GetMapping("/byId/{id}")
	public ResponseEntity<?> getImageById(@PathVariable("id") String id) {
		Image image = imageService.getImageById(id);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(image));
	}

	@GetMapping("/byModelId/{modelId}")
	public ResponseEntity<?> getImagesByModelId(@PathVariable("modelId") String modelId) {
		List<Image> images = imageService.getImagesByModelId(modelId);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(images));
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
		String imageTmp = imageService.uploadImage(image);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(imageTmp));
	}
}
