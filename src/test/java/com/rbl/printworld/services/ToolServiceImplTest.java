package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ToolServiceImplTest {

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
	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\tmp")
			.repositoryData("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data")
			.metaCounter("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\configs\\metaCounter.txt")
			.build();

	@Autowired
	private ToolServiceImpl toolService;

	@Test
	public void transferMultipartFileToFileTmpTest() throws IOException {
		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		MultipartFile multipartFile = new MockMultipartFile(
				"name",
				"test.zip",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.zip")
		);

		String pathFileTmp = toolService.transferMultipartFileToFileTmp(multipartFile, "m-20211222-000001");
		String pathFileTmpExpected = printWorldProperties.getTmp() + File.separator + "tmp_m-20211222-000001.zip";

		Assert.assertNotNull(pathFileTmp);
		Assert.assertEquals("Path tmp file is equal from path tmp file expected", pathFileTmpExpected, pathFileTmp);
		File fileTmp = new File(pathFileTmpExpected);
		if (!fileTmp.exists()) {
			Assert.fail("Test not pass because file tmp isn't create!");
		}
	}

	@Test
	public void getExtensionFileTest() {
		String extensionExpected = "zip";

		toolService.getExtensionFile(expectedModel, "test.zip");

		Assert.assertNotNull(expectedModel.getExtension());
		Assert.assertEquals("The extension expected is equal from extension return by getExtensionFile", extensionExpected, expectedModel.getExtension());
	}

	@Test
	public void saveFileTest() {
		File file = createFileForTest();
		File fileSave = new File(printWorldProperties.getRepositoryData() + File.separator + "2021" + File.separator + "12" + File.separator + "22" + File.separator + "01" + File.separator + "m-20211222-000001.zip");
		toolService.saveFile("m-20211222-000001.zip", file.getAbsolutePath(), "m-20211222-000001");

		log.info(fileSave.getAbsolutePath() + " - " + fileSave.exists());
		if (!fileSave.exists()) {
			Assert.fail("Test not pass because file isn't create!");
		}
	}

	@Test
	public void deleteTest() {
		File fileDelete = new File(printWorldProperties.getRepositoryData() + File.separator + "2021" + File.separator + "12" + File.separator + "22" + File.separator + "01" + File.separator + "m-20211222-000001.zip");
		toolService.deleteFile(fileDelete.getAbsolutePath());

		if (fileDelete.exists()) {
			Assert.fail("Test not pass because file delete exist!");
		}
	}

	// TODO Update id expected for pass test
	@Test
	public void generateIdTest() {
		String idExpected = "m-20211219-000001";
		String id = toolService.generateId();

		Assert.assertNotNull(id);
		Assert.assertEquals("Id of method is equal from id expected", idExpected, id);
	}

	@Test
	public void getPathFileTest() {
		String filename = "m-20211224-000001.zip";
		String pathFileExpected = "C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\2021\\12\\24\\01\\m-20211224-000001.zip";

		String pathFile = toolService.getPathFile(filename, "m-20211224-000001");

		Assert.assertNotNull(pathFile);
		Assert.assertEquals("The path file is equal path file expected", pathFileExpected, pathFile);
	}

	@Test
	public void uploadImagesTest() throws IOException {
		String id = toolService.generateId();
		String year = id.substring(2, 6);
		String month = id.substring(6, 8);
		String day = id.substring(8, 10);
		String folder = id.substring(15, 17);

		File imageTest = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.png");
		try {
			imageTest.getParentFile().mkdirs();
			imageTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		MultipartFile multipartFile = new MockMultipartFile(
				"name",
				"test.png",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.png")
		);

		File imageTest2 = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test2.png");
		try {
			imageTest2.getParentFile().mkdirs();
			imageTest2.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		MultipartFile multipartFile2 = new MockMultipartFile(
				"name",
				"test2.png",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test2.png")
		);

		MultipartFile[] multipartFiles = new MultipartFile[]{multipartFile, multipartFile2};

		File modelTest = new File(printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + folder + File.separator + id + ".zip");
		try {
			modelTest.getParentFile().mkdirs();
			modelTest.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		List<String> imageIds = toolService.uploadImages(multipartFiles, id);

		Assert.assertNotNull("List image id is null!", imageIds);

		File fileTest = new File(printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + folder + File.separator + imageIds.get(0) + ".png");
		if (!fileTest.exists()) {
			Assert.fail("Test not pass because image " + imageIds.get(0) + ".png isn't create!");
		}

		File fileTest2 = new File(printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + folder + File.separator + imageIds.get(1) + ".png");
		if (!fileTest2.exists()) {
			Assert.fail("Test not pass because image " + imageIds.get(0) + ".png isn't create!");
		}
	}

	private File createFileForTest() {
		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return file;
	}
}
