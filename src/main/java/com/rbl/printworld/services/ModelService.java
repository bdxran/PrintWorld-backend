package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.dto.ListResponseDto;

import java.util.List;

public interface ModelService {

	Model getModelById(String id);

	ListResponseDto<Model> getAllModel(Integer page, Integer limit);

	Model createModel(String id, String pathFileTmp, String nameFile, List<String> imageIds, Model model);

	Model modifyModel(String pathFileTmp, Model model);

	boolean deleteModel(Model model);
}
