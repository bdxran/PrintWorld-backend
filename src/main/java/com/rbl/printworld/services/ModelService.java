package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.dto.ListResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ModelService {

	Model getModelById(String id);

	ListResponseDto<Model> getAllModel(Integer page, Integer limit);

	Model createModel(MultipartFile file, MultipartFile[] images, String model);

	Model modifyModel(MultipartFile file, MultipartFile[] images, String model);

	boolean deleteModel(Model model);
}
