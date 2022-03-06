package com.rbl.printworld.controllers;

import com.google.gson.Gson;
import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Access;
import com.rbl.printworld.models.Image;
import com.rbl.printworld.models.User;
import com.rbl.printworld.services.ImageService;
import com.rbl.printworld.services.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

	private final User user = new User("rbl", "rbl@test.com", "test", Access.USER);

	private final ImageService imageService;
	private final ModelService modelService;

	@Autowired
	public ImageController(ImageService imageService, ModelService modelService) {
		this.imageService = imageService;
		this.modelService = modelService;
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

	@GetMapping("/inline/{modelId}")
	public ResponseEntity<?> getInlineImageByModelId(@PathVariable("modelId") String modelId) {
		Image image = imageService.getInlineImagesByModelId(modelId);

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(image));
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

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadImage(@PathVariable("id") String id) {
		Path imagePath = imageService.downloadImage(id);

		try {
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(imagePath));

			return ResponseEntity
					.ok()
					.contentLength(imagePath.toFile().length())
					.contentType(MediaType.IMAGE_JPEG)
					.body(resource);
		} catch (IOException ex) {
			throw new ApplicationException("500", "Error to recover image");
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteImage(@PathVariable("id") String id) {
		Image image = imageService.getImageById(id);
		boolean imageIfdeleted = imageService.deleteImage(image);

		if (imageIfdeleted) {
			modelService.removeIdImage(image.getModelId(), id);
		} else {
			log.error("Image with id " + image.getId() + " isn't delete");
			throw new ApplicationException("500", "Image with id " + image.getId() + " isn't delete");
		}

		MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION)
				.body(new Gson().toJson(image));
	}
}
