package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class ToolServiceImpl implements ToolService {

	private final PrintWorldProperties properties;

	@Autowired
	public ToolServiceImpl(PrintWorldProperties properties) {
		this.properties = properties;
	}

	@Override
	public File transferMultipartFileToFile(MultipartFile multipartFile) {
		log.info("Transfer multipartFile to file : " + multipartFile.getName());
		try {
			File file = new File(this.properties.getTmp() + File.separator + multipartFile.getName());

			multipartFile.transferTo(file);

			return file;
		} catch (IOException ex) {
			throw new ApplicationException("500", "Error when transfer multipartFile to file!");
		}
	}

	@Override
	public File saveFile(File file) {
		return file;
	}
}
