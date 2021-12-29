package com.rbl.printworld.services.impl;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.repositories.ModelRepository;
import com.rbl.printworld.services.ModelService;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

	private final ToolService toolService;
	private final ModelRepository modelRepository;
	private final PrintWorldProperties properties;

	@Autowired
	public ModelServiceImpl(ToolService toolService, ModelRepository modelRepository,
	                        PrintWorldProperties properties) {
		this.toolService = toolService;
		this.modelRepository = modelRepository;
		this.properties = properties;
	}

	/**
	 * Save upload file into the repertory data and new model into DB
	 *
	 * @param id
	 * @param pathFileTmp
	 * @param model
	 * @return new model save into DB
	 */
	@Override
	public Model createModel(String id, String pathFileTmp, String nameFile, Model model) {
		log.info("Save to new model into DB");
		model.setId(id);
		model.setNameFile(nameFile.replace(" ", "_"));

		toolService.saveFile(id, pathFileTmp);

		return modelRepository.save(model);
	}

	/**
	 * Modify upload file into the repertory and model into DB
	 *
	 * @param pathFileTmp
	 * @param model
	 * @return model modified into DB
	 */
	@Override
	public Model modifyModel(String pathFileTmp, Model model) {
		log.info("Modify model into DB");
		String nameFile = model.getNameFile().replace(" ", "_");
		model.setNameFile(nameFile);

		toolService.saveFile(model.getId(), pathFileTmp);

		return modelRepository.save(model);
	}

	/**
	 * Delete model from DB and file from repertory
	 *
	 * @param model
	 * @return status from delete model and file
	 */
	@Override
	public boolean deleteModel(Model model) {
		log.info("Delete model from DB");

		modelRepository.delete(model);
		String pathFile = toolService.getPathFile(model.getId() + "." + model.getExtension());
		toolService.deleteFile(pathFile);

		return true;
	}
}
