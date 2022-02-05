package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.dto.ListResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ModelService {

	Model getModelById(String id);

	ListResponseDto<Model> getAllModel(Integer page, Integer limit);

	Model createModel(MultipartFile file, String[] images, String model);

	Model modifyModel(MultipartFile file, String[] images, String model);

	boolean deleteModel(Model model);

	boolean removeIdImage(String id, String idImage);
}
