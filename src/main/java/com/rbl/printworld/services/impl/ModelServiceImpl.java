package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Model;
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

	@Autowired
	public ModelServiceImpl(ToolService toolService, ModelRepository modelRepository) {
		this.toolService = toolService;
		this.modelRepository = modelRepository;
	}

	@Override
	public Model createModel(File file, Model model) {
		if (!model.getExtension().equals("zip")) {
			log.error("File to send isn't zip!");
			throw new ApplicationException("500", "File upload isn't zip!");
		}
		log.info("Save to new model into DB");
		String nameFile = model.getNameFile().replace(" ", "_");
		model.setNameFile(nameFile);

		toolService.saveFile(file);

		Model modelSave = modelRepository.save(model);

		return modelSave;
	}
}
