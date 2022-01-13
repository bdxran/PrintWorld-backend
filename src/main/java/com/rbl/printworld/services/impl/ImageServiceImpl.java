package com.rbl.printworld.services.impl;

import com.rbl.printworld.models.Image;
import com.rbl.printworld.repositories.ImageRepository;
import com.rbl.printworld.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public void addImage(Image image) {
		log.info("Save a new image into DB : " + image.getName());

		imageRepository.save(image);
	}
}
