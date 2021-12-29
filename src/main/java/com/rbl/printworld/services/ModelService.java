package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;

public interface ModelService {

	Model createModel(String id, String pathFileTmp, String nameFile, Model model);

	Model modifyModel(String pathFileTmp, Model model);

	boolean deleteModel(Model model);
}
