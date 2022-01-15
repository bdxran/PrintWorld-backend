package com.rbl.printworld.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	String addImage(String imageNameFile, String modelId);

	String uploadImage(MultipartFile image);
}
