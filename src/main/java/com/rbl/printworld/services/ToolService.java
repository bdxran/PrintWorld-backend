package com.rbl.printworld.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ToolService {
	File transferMultipartFileToFile(MultipartFile multipartFile);

	File saveFile(File file);
}
