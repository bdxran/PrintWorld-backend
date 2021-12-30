package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.models.dto.ListResponseDto;
import com.rbl.printworld.services.impl.ModelServiceImpl;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongounit.AssertMatchesDataset;
import org.mongounit.MongoUnitTest;
import org.mongounit.SeedWithDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@MongoUnitTest
@RunWith(SpringRunner.class)
@Import({ModelServiceImpl.class, ToolServiceImpl.class})
public class ModelServiceImplTest {

	private final String[] expectedArrayIdModels = new String[]{"m-20211224-000001", "m-20211224-000002"};
	private final Model expectedModel = Model.builder()
			.id("m-20211224-000001")
			.name("testModel")
			.description("blabla")
			.nameFile("test")
			.extension("zip")
			.numberElement(1)
			.note(5)
			.size(1564489)
			.build();
	private final Model expectedModifyModel = Model.builder()
			.id("m-20211224-000001")
			.name("testModel2")
			.description("blabla")
			.nameFile("test")
			.extension("zip")
			.numberElement(1)
			.note(5)
			.size(1564489)
			.build();
	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\tmp")
			.repositoryData("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data")
			.metaCounter("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\configs\\metaCounter.txt")
			.build();

	@Autowired
	private ModelServiceImpl modelService;
	@Autowired
	private ToolServiceImpl toolService;

	@Test
	public void getModelByIdTest() {
		File file = createFileForTest();

		Model saveModel = modelService.createModel(this.expectedModel.getId(), file.getAbsolutePath(), file.getName(), this.expectedModel);

		Model model = modelService.getModelById(this.expectedModel.getId());

		Assert.assertNotNull(model);
		Assert.assertEquals("Model get by id into db is equal from expected model", this.expectedModel, model);
	}

	@Test
	public void getAllModelTest() {
		File file = createFileForTest();
		Model model1 = this.expectedModel;
		Model saveModel1 = modelService.createModel(model1.getId(), file.getAbsolutePath(), file.getName(), model1);

		file = createFileForTest();
		Model model2 = this.expectedModel;
		model2.setId("m-20211224-000002");
		Model saveModel2 = modelService.createModel(model2.getId(), file.getAbsolutePath(), file.getName(), model2);

		ListResponseDto<Model> modelsResponseDto = modelService.getAllModel(null, null);
		List<Model> models = modelsResponseDto.getData();
		String[] arrayIdModels = new String[]{models.get(0).getId(), models.get(1).getId()};

		Assert.assertNotNull(models);
		Assert.assertArrayEquals("Models into db were not equal from expected models", this.expectedArrayIdModels, arrayIdModels);
	}

	@Test
	public void getAllModelPageTest() {
		File file = createFileForTest();
		Model model1 = this.expectedModel;
		Model saveModel1 = modelService.createModel(model1.getId(), file.getAbsolutePath(), file.getName(), model1);

		file = createFileForTest();
		Model model2 = this.expectedModel;
		model2.setId("m-20211224-000002");
		Model saveModel2 = modelService.createModel(model2.getId(), file.getAbsolutePath(), file.getName(), model2);

		ListResponseDto<Model> modelsResponseDto = modelService.getAllModel(0, 10);
		List<Model> models = modelsResponseDto.getData();
		String[] arrayIdModels = new String[]{models.get(0).getId(), models.get(1).getId()};

		Assert.assertNotNull(models);
		Assert.assertArrayEquals("Models into db were not equal from expected models", this.expectedArrayIdModels, arrayIdModels);
	}

	@Test
	public void createModelTest() {
		String id = toolService.generateId();

		File file = createFileForTest();

		Model saveModel = modelService.createModel(id, file.getAbsolutePath(), file.getName(), this.expectedModel);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected isn't equal", saveModel, this.expectedModel);
	}

	@Test
	public void modifyModelTest() {
		File file = createFileForTest();

		Model saveModel = modelService.modifyModel(file.getAbsolutePath(), this.expectedModifyModel);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected isn't equal", saveModel, this.expectedModifyModel);
	}

	@Test
	public void deleteModelTest() {
		createFileForTest();

		boolean checkDeleteModel = modelService.deleteModel(this.expectedModel);

		Assert.assertTrue("Delete model isn't return true", checkDeleteModel);
	}

	private File createFileForTest(){
		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\2021\\12\\24\\01\\m-20211224-000001.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return file;
	}
}
