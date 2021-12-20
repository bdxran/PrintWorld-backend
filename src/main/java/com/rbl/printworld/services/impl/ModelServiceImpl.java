package com.rbl.printworld.services.impl;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.repositories.ModelRepository;
import com.rbl.printworld.services.ModelService;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

	private final ToolService toolService;
	private final ModelRepository modelRepository;

	@Autowired
	public ModelServiceImpl(ToolService toolService, ModelRepository modelRepository) {
		this.toolService = toolService;
		this.modelRepository = modelRepository;
	}

	@Override
	public Model createModel(String id, String pathFileTmp, Model model) {
		log.info("Save to new model into DB");
		model.setId(id);
		String nameFile = model.getNameFile().replace(" ", "_");
		model.setNameFile(nameFile);

		toolService.saveFile(id, pathFileTmp);

		return modelRepository.save(model);
	}

	@Override
	public Model modifyModel(String pathFileTmp, Model model) {
		log.info("Modify model into DB");
		String nameFile = model.getNameFile().replace(" ", "_");
		model.setNameFile(nameFile);

		toolService.saveFile(model.getId(), pathFileTmp);

		return modelRepository.save(model);
	}

	@Override
	public boolean deleteModel(Model model) {
		log.info("Delete model from DB");

		modelRepository.delete(model);
		toolService.deleteFile(model.getId());

		return true;
	}
}
