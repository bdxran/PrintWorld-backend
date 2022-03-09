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
import java.nio.file.Path;
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

	@Override
	public Image getImageById(String id) {
		log.info("Search image by id : " + id);

		return imageRepository.findById(id).orElseThrow(
				() -> new ApplicationException("404", "Image with id " + id + " not found!"));
	}

	@Override
	public List<Image> getImagesByModelId(String modelId) {
		log.info("Search images by model id : " + modelId);

		return imageRepository.findByModelId(modelId).orElseThrow(
				() -> new ApplicationException("404", "Images with model id " + modelId + " not found!"));
	}

	@Override
	public Image getInlineImagesByModelId(String modelId) {
		log.info("Search images by model id : " + modelId);

		return imageRepository.findInlineByModelId(modelId).orElseThrow(
				() -> new ApplicationException("404", "Images with model id " + modelId + " not found!"));
	}

	/**
	 * Allow save image to tmp directory
	 *
	 * @param imageNameFile
	 * @return name image tmp
	 * @parma modelId
	 */
	@Override
	public String addImage(String imageNameFile, String modelId, boolean inline) {
		log.info("Save a new image : " + imageNameFile);

		String imageTmp = "tmp_" + imageNameFile;
		String imageTmpPath = this.properties.getTmp() + File.separator + imageTmp;
		String extension = imageNameFile.substring(imageNameFile.lastIndexOf(".") + 1);
		String imageId = toolService.generateId();
		String imageName = imageNameFile.substring(0, imageNameFile.lastIndexOf("."));

		Image image = Image.builder()
				.id(imageId)
				.name(imageName)
				.extension(extension)
				.modelId(modelId)
				.inline(inline)
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
	@Override
	public String uploadImage(MultipartFile image) {
		log.info("Upload image to tmp directory");

		return toolService.transferMultipartFileToImageTmp(image);
	}

	@Override
	public Path downloadImage(String id) {
		log.info("Download image by id : " + id);
		Image image = getImageById(id);

		String filename = id + "." + image.getExtension();
		String pathFile = toolService.getPathFile(filename, image.getModelId());

		log.info("Recover image to path : " + pathFile);
		return new File(pathFile).toPath();
	}

	@Override
	public boolean deleteImage(Image image) {
		log.info("Delete image from DB");
		imageRepository.delete(image);

		String pathFile = toolService.getPathFile(image.getId() + "." + image.getExtension(), image.getModelId());
		toolService.deleteFile(pathFile);

		return true;
	}
}
