package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
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

@SpringBootTest
@MongoUnitTest
@RunWith(SpringRunner.class)
@Import({ModelServiceImpl.class, ToolServiceImpl.class})
public class ModelServiceImplTest {

	private final Model expectedCreateModel = Model.builder()
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
	@AssertMatchesDataset("createModel.json")
	public void createModelTest() {
		String id = toolService.generateId();

		Model model = Model.builder()
				.name("testModel")
				.description("blabla")
				.nameFile("test")
				.extension("zip")
				.numberElement(1)
				.note(5)
				.size(1564489)
				.build();

		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		Model saveModel = modelService.createModel(id, file.getAbsolutePath(), model);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected is equal", saveModel, this.expectedCreateModel);
	}

	@Test
	@SeedWithDataset("modelStart.json")
	@AssertMatchesDataset("modifyModel.json")
	public void modifyModelTest() {
		Model model = Model.builder()
				.id("m-20211224-000001")
				.name("testModel2")
				.description("blabla")
				.nameFile("test")
				.extension("zip")
				.numberElement(1)
				.note(5)
				.size(1564489)
				.build();

		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		Model saveModel = modelService.modifyModel(file.getAbsolutePath(), model);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected is equal", saveModel, this.expectedModifyModel);
	}

	@Test
	@SeedWithDataset("modelStart.json")
	@AssertMatchesDataset("deleteModel.json")
	public void deleteModelTest() {
		Model model = Model.builder()
				.id("m-20211224-000001")
				.name("testModel")
				.description("blabla")
				.nameFile("test")
				.extension("zip")
				.numberElement(1)
				.note(5)
				.size(1564489)
				.build();

		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\2021\\12\\24\\01\\m-20211224-000001.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		boolean checkDeleteModel = modelService.deleteModel(model);

		Assert.assertTrue("Model save and model expected is equal", checkDeleteModel);
	}
}
