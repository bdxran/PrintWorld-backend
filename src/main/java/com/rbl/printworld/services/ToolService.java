package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import org.springframework.web.multipart.MultipartFile;

public interface ToolService {
	String transferMultipartFileToFile(MultipartFile multipartFile, String id);

	void getExtensionFile(Model model, String nameFile);

	void saveFile(String id, String pathFileTmp);

	void deleteFile(String pathFile);

	String generateId();
}
