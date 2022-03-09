package com.rbl.printworld.services;

import com.rbl.printworld.models.Image;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ImageServiceImpl;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@MongoUnitTest
@RunWith(SpringRunner.class)
@Import({ImageServiceImpl.class, ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ImageServiceImplTest {

	@Autowired
	private ImageServiceImpl imageService;
	@Autowired
	private ToolServiceImpl toolService;

	private String pattern = "yyyyMMdd";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	private String date = simpleDateFormat.format(new Date());

	private final Image imageExpected = Image.builder()
			.id("m-" + date + "-000002")
			.name("test")
			.extension("png")
			.modelId("m-" + date + "-000001")
			.build();
	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("./tmp")
			.repositoryData("./data")
			.metaCounter("./configs/metaCounter.txt")
			.environment("test")
			.build();

	@Test
	public void getImageByIdTest() {
		addImage();

		Image image = imageService.getImageById(imageExpected.getId());

		Assert.assertNotNull("Image is null!", image);
		Assert.assertEquals("Image isn't equals with image expected!", imageExpected, image);
	}

	@Test
	public void getImageByModelIdTest() {
		addImage();
		List<Image> imagesExpected = Collections.singletonList(imageExpected);

		List<Image> images = imageService.getImagesByModelId(imageExpected.getModelId());

		Assert.assertNotNull("Image is null!", images);
		Assert.assertEquals("Image isn't equals with image expected!", imagesExpected, images);
	}

	@Test
	public void getInlineImagesByModelId() {
		Map<String, String> result = addImage();
		Image imageExpected = Image.builder()
				.id(result.get("imageId"))
				.modelId(result.get("modelId"))
				.name("test")
				.extension("png")
				.inline(true)
				.build();

		Image image = imageService.getInlineImagesByModelId(result.get("modelId"));

		Assert.assertNotNull(image);
		Assert.assertEquals("Image is not equals with image expected!", imageExpected, image);
	}

	@Test
	public void addImageTest() {
		toolService.setMetaCounter(1);
		String modelId = toolService.generateId();
		String imageName = "test.png";

		File imageTest = new File(printWorldProperties.getTmp() + File.separator + "tmp_" + imageName);
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		String id = imageService.addImage(imageName, modelId, true);

		Assert.assertNotNull("AddImage return id null!", id);
		String pathImageExpected = toolService.getPathFile(id + ".png", modelId);
		File imageExpected = new File(pathImageExpected);

		if (!imageExpected.exists()) {
			Assert.fail("Test not pass because image test not exist!");
		}
	}

	@Test
	public void uploadImageTest() throws IOException {
		toolService.setMetaCounter(1);
		File imageTest = new File("./data/test.png");
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		MultipartFile multipartFile = new MockMultipartFile(
				"test",
				"test.png",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("./data/test.png")
		);

		String imageTmpTest = imageService.uploadImage(multipartFile);

		Assert.assertNotNull("Path tmp image is null!", imageTmpTest);

		File fileTest = new File("./tmp/" + imageTmpTest);
		if (!fileTest.exists()) {
			Assert.fail("Test not pass because image " + imageTmpTest + " isn't create!");
		}
	}

	@Test
	public void downloadImageTest(){
		Map<String, String> result = addImage();
		Path pathImageExpected = new File("data\\2022\\03\\09\\01\\" + result.get("imageId") + ".png").toPath();

		Path pathImage = imageService.downloadImage(result.get("imageId"));

		Assert.assertNotNull("Path image for download image is null", pathImage);
		Assert.assertEquals("Path is not equals with path expected", pathImageExpected, pathImage);
	}

	@Test
	public void deleteImage(){
		Map<String, String> result = addImage();
		Image imageExpected = Image.builder()
				.id(result.get("imageId"))
				.modelId(result.get("modelId"))
				.name("test")
				.extension("png")
				.inline(true)
				.build();

		imageService.deleteImage(imageExpected);

		File fileTest = new File("data\\2022\\03\\09\\01\\" + result.get("imageId") + ".png");
		if (fileTest.exists()) {
			Assert.fail("Test not pass because image " + imageExpected.getName() + " isn't delete!");
		}
	}

	private Map<String, String> addImage() {
		toolService.setMetaCounter(1);
		String modelId = toolService.generateId();
		String imageName = "test.png";

		File imageTest = new File(printWorldProperties.getTmp() + File.separator + "tmp_" + imageName);
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		String id = imageService.addImage(imageName, modelId, true);

		Assert.assertNotNull("AddImage return id null!", id);
		String pathImageExpected = toolService.getPathFile(id + ".png", modelId);
		File imageExpected = new File(pathImageExpected);

		if (!imageExpected.exists()) {
			Assert.fail("Test not pass because image test not exist!");
		}

		Map<String, String> result = new HashMap<>();
		result.put("modelId", modelId);
		result.put("imageId", id);

		return result;
	}
}
