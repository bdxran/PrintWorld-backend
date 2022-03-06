package com.rbl.printworld.services;

import com.rbl.printworld.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface ImageService {

	Image getImageById(String id);

	List<Image> getImagesByModelId(String modelId);

	Image getInlineImagesByModelId(String modelId);

	String addImage(String imageNameFile, String modelId, boolean inline);

	String uploadImage(MultipartFile image);

	Path downloadImage(String image);

	boolean deleteImage(Image image);
}
