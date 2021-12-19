package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;

public interface ModelService {

	Model createModel(String id, String pathFileTmp, Model model);
}
