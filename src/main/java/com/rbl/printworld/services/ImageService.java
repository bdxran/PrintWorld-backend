package com.rbl.printworld.services;

import com.rbl.printworld.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

	Image getImageById(String id);

	List<Image> getImagesByModelId(String modelId);

	String addImage(String imageNameFile, String modelId);

	String uploadImage(MultipartFile image);
}
