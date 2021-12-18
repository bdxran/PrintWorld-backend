package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
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
	public Model createModel(String pathFileTmp, Model model) {
		toolService.getExtensionFile(model);
		if (!model.getExtension().equals("zip")) {
			log.warn("File to send isn't zip, is : " + model.getExtension());
			throw new ApplicationException("415", "File upload isn't zip!");
		}

		log.info("Save to new model into DB");

		String nameFile = model.getNameFile().replace(" ", "_");
		model.setNameFile(nameFile);

		toolService.saveFile(pathFileTmp, model);

		return modelRepository.save(model);
	}
}
