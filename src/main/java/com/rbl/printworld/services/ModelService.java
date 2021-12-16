package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;

import java.io.File;

public interface ModelService {

	Model createModel(File file, Model model);
}
