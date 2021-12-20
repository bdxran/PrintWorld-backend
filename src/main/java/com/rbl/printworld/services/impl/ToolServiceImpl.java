package com.rbl.printworld.services.impl;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
public class ToolServiceImpl implements ToolService {

	private int metaCounter;
	private final String[] base32 = {"0","1","2","3","4","5","6","7","8","9","A","C","D","E","F","G","H","J","K","L","M",
			"N","P","Q","R","S","T","U","V","W","X","Y","Z"};

	private final PrintWorldProperties properties;

	@Autowired
	public ToolServiceImpl(PrintWorldProperties properties) {
		this.properties = properties;
	}

	@Override
	public String transferMultipartFileToFile(MultipartFile multipartFile, String id) {
		log.info("Transfer multipartFile to file : " + multipartFile.getOriginalFilename());
		try {
			File file = new File(this.properties.getTmp() + File.separator + "tmp_" + id + ".zip");

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
	public void saveFile(String id, String pathFileTmp) {
		String filename = id + ".zip";
		copyFile(filename, pathFileTmp);
	}

	@Override
	public void deleteFile(String id) {
		//TODO
	}

	@Override
	public String generateId() {
		String pattern = "yyyyMMdd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		String code32 = encode32();

		String genId = "m-" + date + "-" + code32;

		indentMetaCounter();

		return genId;
	}

	/***
	 * Copy only file. Don't use for folder because copy folder without content.
	 * @param filename
	 * @param toCopied
	 */
	private void copyFile(String filename, String toCopied) {
		String pathFilename = getPathFile(filename);
		File fileToCopied = new File(toCopied);
		if (!fileToCopied.exists()) {
			log.error("File is not found : {}", fileToCopied.getPath());
			throw new ApplicationException("404", "File is not found : " + fileToCopied.getPath());
		}
		File copyFile = new File(pathFilename);

		Path source = Paths.get(fileToCopied.getAbsolutePath());
		Path target = Paths.get(copyFile.getAbsolutePath());

		try {
			Files.copy(source, target, REPLACE_EXISTING);
			log.info("File or folder {} has moved to {}", filename, copyFile.getPath());
		} catch (IOException e) {
			log.error("Cannot copy file/folder {} to {}, error {}", filename, copyFile.getPath(), e);
			throw new ApplicationException("500", "Cannot move file/folder " + filename + " to " + copyFile.getPath());
		}
	}

	private String encode32() {
		if (this.metaCounter <= 0) {
			getMetaCounter();
		}
		int num = this.metaCounter;
		StringBuilder code = new StringBuilder();

		while(num >= 1) {
			code.append(this.base32[num % 32]);
			//code += (base32.charAt(num % base32.length()));
			num = num/32;
		}
		while (code.length()<6){
			code.append("0");
		}
		code.reverse();

		log.info("Code for new id is : " + code);

		return code.toString();
	}

	private String decode32(String code) {
		//TODO
		return null;
	}

	private void getMetaCounter() {
		log.info("Get metaCounter");
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.properties.getMetaCounter()));
			String line = "";
			while ((line = br.readLine()) != null) {
				this.metaCounter = Integer.parseInt(line);
				log.info("Meta counter is : " + this.metaCounter);
				break;
			}
		} catch (FileNotFoundException ex) {
			log.error("MetaCounter file isn't found!");
			throw new ApplicationException("500", "MetaCounter file isn't found!");
		} catch (IOException ex) {
			log.error("Error when read or close metaCounter file!");
			throw new ApplicationException("500", "Error when read or close metaCounter file!");
		} catch (Exception ex) {
			log.error("MetaCounter file is void!" + ex.getMessage());
			throw new ApplicationException("500", "MetaCounter file is void!");
		}
	}

	private String getPathFile(String filename) {
		String year = filename.substring(2,6);
		String month = filename.substring(6,8);
		String day = filename.substring(8,10);
		String folder = filename.substring(15,17);
		String path = this.properties.getRepositoryData() + File.separator + year + File.separator + month
				+ File.separator + day + File.separator + folder;

		createFolder(path);

		return path + File.separator + filename;
	}

	private boolean createFolder(String path) {
		path = path.replaceAll("\\\\", "\\/");
		log.debug("Path to check : {}", path);
		Pattern p = Pattern.compile("^*\\.\\w*$");
		Matcher m = p.matcher(path);
		if (m.find()) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
			log.info("Create repository : {}", path);
			return true;
		}
		return false;
	}

	private void indentMetaCounter() {
		this.metaCounter++;
	}
}
