package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ToolServiceImpl implements ToolService {

	private final PrintWorldProperties properties;

	@Autowired
	public ToolServiceImpl(PrintWorldProperties properties) {
		this.properties = properties;
	}

	@Override
	public String transferMultipartFileToFile(MultipartFile multipartFile) {
		log.info("Transfer multipartFile to file : " + multipartFile.getOriginalFilename());
		try {
			String nameMultiPartFile = multipartFile.getOriginalFilename().replace(" ", "_");
			File file = new File(this.properties.getTmp() + File.separator + "tmp_" + nameMultiPartFile);

			multipartFile.transferTo(file);

			return file.getAbsolutePath();
		} catch (IOException ex) {
			throw new ApplicationException("500", "Error when transfer multipartFile to file!");
		}
	}

	@Override
	public void getExtensionFile(Model model) {
		log.info("Get extension file");

		String nameFile = model.getNameFile();
		String extension = nameFile.substring(nameFile.lastIndexOf(".") + 1);
		model.setExtension(extension);
	}

	@Override
	public void saveFile(String pathFileTmp, Model model) {
		String genId = generateId();

		File fileTmp = new File(pathFileTmp);
	}

	private String generateId() {
		String pattern = "yyyyMMdd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		String code32 = generateCode32();

		String genId = "m-" + date + "-";

		return genId;
	}

	private String generateCode32() {
		String metaCounter = getMetaCounter();

		return null;
	}

	private String getMetaCounter() {
		log.info("Get metaCounter");
		String metaCounter = "";

		try {
			FileInputStream inputStream = new FileInputStream(this.properties.getMetaCounter());
			try {
				metaCounter = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			} finally {
				inputStream.close();
			}
		} catch (FileNotFoundException ex) {
			log.error("MetaCounter file isn't found!");
			throw new ApplicationException("500", "MetaCounter file isn't found!");
		} catch (IOException ex) {
			log.error("Error when read or close metaCounter file!");
			throw new ApplicationException("500", "Error when read or close metaCounter file!");
		}

		if (metaCounter.equals("")) {
			log.error("MetaCounter file is void!");
			throw new ApplicationException("500", "MetaCounter file is void!");
		}

		return metaCounter;
	}
}
