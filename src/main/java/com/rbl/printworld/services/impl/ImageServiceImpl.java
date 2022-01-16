package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Image;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.repositories.ImageRepository;
import com.rbl.printworld.services.ImageService;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;
	private final ToolService toolService;
	private final PrintWorldProperties properties;

	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository, ToolService toolService,
	                        PrintWorldProperties properties) {
		this.imageRepository = imageRepository;
		this.toolService = toolService;
		this.properties = properties;
	}

	public Image getImageById(String id){
		log.info("Search image by id : " + id);

		return imageRepository.findById(id).orElseThrow(
				() -> new ApplicationException("404", "Image with id " + id + " not found!"));
	}

	public List<Image> getImagesByModelId(String modelId){
		log.info("Search images by model id : " + modelId);

		return imageRepository.findByModelId(modelId).orElseThrow(
				() -> new ApplicationException("404", "Images with model id " + modelId + " not found!"));
	}

	/**
	 * Allow save image to tmp directory
	 *
	 * @param imageNameFile
	 * @parma modelId
	 * @return name image tmp
	 */
	public String addImage(String imageNameFile, String modelId) {
		log.info("Save a new image");

		String imageTmpPath = this.properties.getTmp() + File.separator + imageNameFile;
		String extension = imageNameFile.substring(imageNameFile.lastIndexOf(".") + 1);
		String imageId = toolService.generateId();
		String imageName = imageNameFile.substring(0, imageNameFile.lastIndexOf("."));

		Image image = Image.builder()
				.id(imageId)
				.name(imageName)
				.extension(extension)
				.modelId(modelId)
				.build();

		String filename = imageId + "." + extension;

		log.info("Move image tmp : " + image.getName() + " to model directory : " + modelId);
		toolService.saveFile(filename, imageTmpPath, modelId);

		log.info("Save image : " + image.getName() + " into DB");
		Image imageSave = imageRepository.save(image);

		return imageSave.getId();
	}

	/**
	 * Allow save image to tmp directory
	 *
	 * @param image
	 * @return name image tmp
	 */
	public String uploadImage(MultipartFile image) {
		log.info("Upload image to tmp directory");

		return toolService.transferMultipartFileToImageTmp(image);
	}

	public void deleteImage() {
		//TODO
	}
}
