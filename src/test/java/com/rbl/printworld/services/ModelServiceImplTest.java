package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.models.dto.ListResponseDto;
import com.rbl.printworld.services.impl.ModelServiceImpl;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongounit.MongoUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
@MongoUnitTest
@RunWith(SpringRunner.class)
@Import({ModelServiceImpl.class, ToolServiceImpl.class})
public class ModelServiceImplTest {

	private String pattern = "yyyyMMdd";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	private String date = simpleDateFormat.format(new Date());
	private final String[] expectedArrayIdModels = new String[]{"m-" + date + "-000001", "m-" + date + "-000002"};
	private final List<String> imageIds = Collections.singletonList("m-" + date + "-000002");
	private final List<Integer> subCategoryIds = new ArrayList<>() {{
		add(1);
		add(2);
	}};
	private final Model expectedModel = Model.builder()
			.id("m-" + date + "-000001")
			.name("testModel")
			.description("blabla")
			.nameFile("test")
			.extension("zip")
			.numberElement(1)
			.note(5)
			.size(1564489)
			.categoryId(1)
			.subCategoryIds(subCategoryIds)
			.imageIds(imageIds)
			.build();
	private final Model expectedModifyModel = Model.builder()
			.id("m-" + date + "-000001")
			.name("testModel2")
			.description("plopplop")
			.nameFile("test")
			.extension("zip")
			.numberElement(1)
			.note(5)
			.size(1564489)
			.categoryId(1)
			.subCategoryIds(subCategoryIds)
			.imageIds(imageIds)
			.build();
	private final String expectedModelJson = "{\n" +
			"    \"name\": \"testModel\",\n" +
			"    \"description\": \"blabla\",\n" +
			"    \"nameFile\": \"test\",\n" +
			"    \"numberElement\": 1,\n" +
			"    \"note\": 5,\n" +
			"    \"size\": 1564489,\n" +
			"    \"categoryId\": 1,\n" +
			"    \"subCategoryIds\": [1, 2]\n" +
			"}";
	private final String expectedModifyModelJson = "{\n" +
			"    \"id\": \"m-" + date + "-000001\",\n" +
			"    \"name\": \"testModel2\",\n" +
			"    \"description\": \"plopplop\",\n" +
			"    \"nameFile\": \"test\",\n" +
			"    \"numberElement\": 1,\n" +
			"    \"note\": 5,\n" +
			"    \"size\": 1564489,\n" +
			"    \"categoryId\": 1,\n" +
			"    \"subCategoryIds\": [1, 2]\n" +
			"}";
	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\tmp")
			.repositoryData("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data")
			.metaCounter("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\configs\\metaCounter.txt")
			.build();

	@Autowired
	private ModelServiceImpl modelService;
	@Autowired
	private ToolServiceImpl toolService;

	@Test
	public void getModelByIdTest() throws IOException {
		MultipartFile multipartFileTest = createMultipartFileTest("testFile");
		MultipartFile multipartImageTest = createMultipartImageTest("testImage");
		MultipartFile[] multipartImagesTest = new MultipartFile[]{multipartImageTest};
		File fileTest = createFileForTest();

		Model saveModel = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		Model model = modelService.getModelById(saveModel.getId());

		Assert.assertNotNull(model);
		Assert.assertEquals("Model get by id into db is equal from expected model", this.expectedModel, model);
	}

	@Test
	public void getAllModelTest() throws IOException {
		MultipartFile multipartFileTest = createMultipartFileTest("testFile");
		MultipartFile multipartImageTest = createMultipartImageTest("testImage");
		MultipartFile[] multipartImagesTest = new MultipartFile[]{multipartImageTest};

		File file = createFileForTest();
		Model model1 = this.expectedModel;
		Model saveModel1 = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		file = createFileForTest();
		Model model2 = this.expectedModel;
		Model saveModel2 = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		ListResponseDto<Model> modelsResponseDto = modelService.getAllModel(null, null);
		List<Model> models = modelsResponseDto.getData();
		String[] arrayIdModels = new String[]{models.get(0).getId(), models.get(1).getId()};

		Assert.assertNotNull(models);
		Assert.assertArrayEquals("Models into db were not equal from expected models", this.expectedArrayIdModels, arrayIdModels);
	}

	@Test
	public void getAllModelPageTest() throws IOException {
		MultipartFile multipartFileTest = createMultipartFileTest("testFile");
		MultipartFile multipartImageTest = createMultipartImageTest("testImage");
		MultipartFile[] multipartImagesTest = new MultipartFile[]{multipartImageTest};

		File file = createFileForTest();
		Model model1 = this.expectedModel;
		Model saveModel1 = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		file = createFileForTest();
		Model model2 = this.expectedModel;
		Model saveModel2 = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		ListResponseDto<Model> modelsResponseDto = modelService.getAllModel(0, 10);
		List<Model> models = modelsResponseDto.getData();
		String[] arrayIdModels = new String[]{models.get(0).getId(), models.get(1).getId()};

		Assert.assertNotNull(models);
		Assert.assertArrayEquals("Models into db were not equal from expected models", this.expectedArrayIdModels, arrayIdModels);
	}

	@Test
	public void createModelTest() throws IOException {
		MultipartFile multipartFileTest = createMultipartFileTest("test");
		MultipartFile multipartImageTest = createMultipartImageTest("testImage");
		MultipartFile[] multipartImagesTest = new MultipartFile[]{multipartImageTest};

		Model saveModel = modelService.createModel(multipartFileTest, multipartImagesTest, this.expectedModelJson);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected isn't equal", saveModel, this.expectedModel);
	}

	@Test
	public void modifyModelTest() throws IOException {
		MultipartFile multipartFileTest = createMultipartFileTest("test");
		MultipartFile multipartImageTest = createMultipartImageTest("testImage");
		MultipartFile[] multipartImagesTest = new MultipartFile[]{multipartImageTest};
		toolService.generateId();

		Model saveModel = modelService.modifyModel(multipartFileTest, multipartImagesTest, this.expectedModifyModelJson);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected isn't equal", saveModel, this.expectedModifyModel);
	}

	@Test
	public void deleteModelTest() {
		createFileForTest();

		boolean checkDeleteModel = modelService.deleteModel(this.expectedModel);

		Assert.assertTrue("Delete model isn't return true", checkDeleteModel);
	}

	private File createFileForTest() {
		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\2021\\12\\24\\01\\m-20211224-000001.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return file;
	}

	private MultipartFile createMultipartFileTest(String name) throws IOException {
		File imageTest = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\" + name + ".zip");
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return new MockMultipartFile(
				name,
				name + ".zip",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\" + name + ".zip")
		);
	}

	private MultipartFile createMultipartImageTest(String name) throws IOException {
		File imageTest = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\" + name + ".png");
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return new MockMultipartFile(
				name,
				name + ".png",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\" + name + ".png")
		);
	}
}
