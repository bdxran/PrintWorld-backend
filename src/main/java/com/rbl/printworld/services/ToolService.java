package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ToolService {
	String transferMultipartFileToFileTmp(MultipartFile multipartFile, String id);

	void getExtensionFile(Model model, String nameFile);

	void saveFile(String filename, String pathFileTmp, String id);

	void deleteFile(String pathFile);

	String generateId();

	String getPathFile(String filename, String id);

	List<String> uploadImages(MultipartFile[] images, String id);
}
