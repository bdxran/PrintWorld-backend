package com.rbl.printworld.services.impl;

import com.google.gson.Gson;
import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.models.dto.ListResponseDto;
import com.rbl.printworld.models.enums.RequestStatus;
import com.rbl.printworld.repositories.ModelRepository;
import com.rbl.printworld.services.ImageService;
import com.rbl.printworld.services.ModelService;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

	private final ToolService toolService;
	private final ModelRepository modelRepository;
	private final PrintWorldProperties properties;
	private final ImageService imageService;

	@Autowired
	public ModelServiceImpl(ToolService toolService, ModelRepository modelRepository,
	                        PrintWorldProperties properties, ImageService imageService) {
		this.toolService = toolService;
		this.modelRepository = modelRepository;
		this.properties = properties;
		this.imageService = imageService;
	}

	@Override
	public Model getModelById(String id) {
		log.info("Search model by id : " + id);
		return modelRepository.findById(id).orElseThrow(
				() -> new ApplicationException("404", "Model with id " + id + " not found!"));
	}

	@Override
	public ListResponseDto<Model> getAllModel(Integer page, Integer limit) {
		if (page != null && limit != null) {
			log.info("Recover all model in page : " + page + " with limit : " + limit);
			Page<Model> modelPage = modelRepository.findAll(PageRequest.of(page, limit));
			return new ListResponseDto<>(RequestStatus.SUCCESS, modelPage.getTotalElements(), modelPage.getTotalPages(), modelPage.getContent());
		}
		log.info("Recover all model");
		List<Model> models = modelRepository.findAll();
		return new ListResponseDto<>(RequestStatus.SUCCESS, (long) models.size(), 1, models);
	}

	/**
	 * Save upload file into the repertory data and new model into DB
	 *
	 * @param modelJson
	 * @return new model save into DB
	 */
	@Override
	public Model createModel(MultipartFile file, String[] images, String modelJson) {
		log.info("Call to create new model");
		Gson gson = new Gson();
		Model model = gson.fromJson(modelJson, Model.class);

		String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
		toolService.getExtensionFile(model, file.getOriginalFilename());
		if (!model.getExtension().equals("zip")) {
			log.warn("File to send isn't zip, is : " + model.getExtension());
			throw new ApplicationException("415", "File upload isn't zip!");
		}

		String id = toolService.generateId();
		String pathFileTmp = toolService.transferMultipartFileToFileTmp(file, id);
		List<String> imageIds = new ArrayList<>();
		for (String image : images) {
			log.info("Treat image : " + image);
			imageIds.add(imageService.addImage(image, id));
		}

		model.setId(id);
		model.setNameFile(filename.replace(" ", "_"));
		model.setImageIds(imageIds);

		String filenameSave = model.getId() + ".zip";
		toolService.saveFile(filenameSave, pathFileTmp, id);

		return modelRepository.save(model);
	}

	/**
	 * Modify upload file into the repertory and model into DB
	 *
	 * @param file
	 * @param images
	 * @param modelJson
	 * @return model modified into DB
	 */
	@Override
	public Model modifyModel(MultipartFile file, String[] images, String modelJson) {
		log.info("Call to update model");
		Gson gson = new Gson();
		Model modelModify = gson.fromJson(modelJson, Model.class);
		Model model = getModelById(modelModify.getId());

		if (file != null) {
			log.info("Change model file by : " + file.getOriginalFilename());
			toolService.getExtensionFile(model, file.getOriginalFilename());
			if (!model.getExtension().equals("zip")) {
				log.warn("File to send isn't zip, is : " + model.getExtension());
				throw new ApplicationException("415", "File upload isn't zip!");
			}
			String pathFileTmp = toolService.transferMultipartFileToFileTmp(file, model.getId());
			String nameFile = file.getOriginalFilename().replace(" ", "_");
			model.setNameFile(nameFile);
			String filename = model.getId() + ".zip";
			toolService.saveFile(filename, pathFileTmp, model.getId());
		}

		if (images != null) {
			List<String> imageIds = model.getImageIds();
			for (int i = 0; i < images.length; i++) {
				log.info(images[i]);
				imageIds.add(imageService.addImage(images[i], model.getId()));
			}
			model.setImageIds(imageIds);
		}

		model.setName(modelModify.getName());
		model.setDescription(modelModify.getDescription());
		model.setNote(modelModify.getNote());
		model.setCategoryId(modelModify.getCategoryId());
		model.setSubCategoryIds(modelModify.getSubCategoryIds());

		return modelRepository.save(model);
	}

	/**
	 * Delete model from DB and file from repertory
	 *
	 * @param model
	 * @return status from delete model and file
	 */
	@Override
	public boolean deleteModel(Model model) {
		log.info("Delete model from DB");

		modelRepository.delete(model);
		String pathFile = toolService.getPathFile(model.getId() + "." + model.getExtension(), model.getId());
		toolService.deleteFile(pathFile);

		return true;
	}

	@Override
	public boolean removeIdImage(String id, String idImage) {
		Model model = getModelById(id);

		List<String> imageIds = model.getImageIds();
		boolean check = imageIds.remove(idImage);

		if (check)
			model.setImageIds(imageIds);

		modelRepository.save(model);

		return check;
	}

	@Override
	public Resource downloadModel(String id) {
		log.info("Download model with id : " + id);
		Model model = getModelById(id);
		String filename = id + "." + model.getExtension();

		String pathModel = toolService.getPathFile(filename, id);
		pathModel = pathModel.substring(0, pathModel.lastIndexOf(File.separator));

		try {
			log.info("Retrieve zip model with path : " + pathModel + " and with filename : " + filename);
			Path file = Paths.get(pathModel).resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new ApplicationException("500", "Could not read the file : " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new ApplicationException("500", "Error: " + ex.getMessage());
		}
	}
}
