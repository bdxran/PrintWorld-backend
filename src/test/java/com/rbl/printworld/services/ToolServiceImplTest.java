package com.rbl.printworld.services;

import com.rbl.printworld.exceptions.ApplicationException;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@MongoUnitTest
@RunWith(SpringRunner.class)
@Import({ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ToolServiceImplTest {

	private File currentDirFile = new File(".");
	private String pathRoot = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().lastIndexOf(File.separator));
	private String pattern = "yyyyMMdd";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	private String date = simpleDateFormat.format(new Date());
	private String year = date.substring(0, 4);
	private String month = date.substring(4, 6);
	private String day = date.substring(6, 8);
	private final Model expectedModel = Model.builder()
			.id("m-" + date + "-000001")
			.name("testModel")
			.description("blabla")
			.nameFile("test")
			.extension("zip")
			.numberElement(1)
			.note(5)
			.size(1564489)
			.build();
	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("tmp")
			.repositoryData("data")
			.metaCounter("configs/metaCounter.txt")
			.environment("test")
			.build();

	@Autowired
	private ToolServiceImpl toolService;

	@Test
	public void transferMultipartFileToFileTmpTest() throws IOException {
		File file = new File("data/test.zip");
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
				new FileInputStream("./data/test.zip")
		);

		String pathFileTmp = toolService.transferMultipartFileToFileTmp(multipartFile, "m-" + date + "-000001");
		String pathFileTmpExpected = this.pathRoot + File.separator + printWorldProperties.getTmp() + File.separator + "tmp_m-" + date + "-000001.zip";

		Assert.assertNotNull(pathFileTmp);
		Assert.assertNotEquals("Path tmp is void !", pathFileTmp, "");
		Assert.assertEquals("Path tmp file is equal from path tmp file expected", pathFileTmpExpected, pathFileTmp);
		File fileTmp = new File(pathFileTmpExpected);
		if (!fileTmp.exists()) {
			Assert.fail("Test not pass because file tmp isn't create!");
		}
	}

	@Test
	public void transferMultipartFileToFileTmpTestErrorVoidId() throws IOException {
		File file = new File("data/test.zip");
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
				new FileInputStream("./data/test.zip")
		);

		ApplicationException applicationException = Assert.assertThrows(ApplicationException.class, () -> {
			toolService.transferMultipartFileToFileTmp(multipartFile, "");
		});
		Assert.assertEquals("Error code is not 500 !", "500", applicationException.getErrorCode());
		Assert.assertTrue(applicationException.getMessage().contains("Past id is void!"));
	}

	@Test
	public void transferMultipartFileToImageTmpTest() throws IOException {
		File file = new File("data/testblabla.jpeg");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		MultipartFile multipartFile = new MockMultipartFile(
				"name",
				"testblabla.jpeg",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("./data/testblabla.jpeg")
		);

		String nameFileTmp = toolService.transferMultipartFileToImageTmp(multipartFile);
		String nameFileTmpExpected = "tmp_" + file.getName();

		Assert.assertNotNull(nameFileTmp);
		Assert.assertNotEquals("Path tmp is void !", nameFileTmp, "");
		Assert.assertEquals("Path tmp file is equal from path tmp file expected", nameFileTmpExpected, nameFileTmp);
		File fileTmp = new File(this.pathRoot + File.separator + this.printWorldProperties.getTmp() + File.separator + nameFileTmpExpected);
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
		String pathFile = toolService.getPathFile("m-" + date + "-000001.zip", "m-" + date + "-000001");
		File fileSave = new File(pathFile);
		toolService.saveFile("m-" + date + "-000001.zip", file.getAbsolutePath(), "m-" + date + "-000001");

		log.info(fileSave.getAbsolutePath() + " - " + fileSave.exists());
		if (!fileSave.exists()) {
			Assert.fail("Test not pass because file isn't create!");
		}
	}

	@Test
	public void deleteTest() {
		String pathFile = toolService.getPathFile("m-" + date + "-000001.zip", "m-" + date + "-000001");
		File fileDelete = new File(pathFile);
		try {
			fileDelete.getParentFile().mkdirs();
			fileDelete.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		toolService.deleteFile(fileDelete.getAbsolutePath());

		if (fileDelete.exists()) {
			Assert.fail("Test not pass because file delete exist!");
		}
	}

	@Test
	public void deleteErrorTest() {
		String pathFile = toolService.getPathFile("m-" + date + "-000042.zip", "m-" + date + "-000042");

		ApplicationException applicationException = Assert.assertThrows(ApplicationException.class, () -> {
			toolService.deleteFile(pathFile);
		});
		Assert.assertEquals("Error code is not 500 !", "500", applicationException.getErrorCode());
		Assert.assertTrue(applicationException.getMessage().contains("File " + pathFile + " isn't delete"));
	}

	@Test
	public void generateIdTest() {
		String idExpected = "m-" + date + "-000001";
		String id = toolService.generateId();

		Assert.assertNotNull(id);
		Assert.assertEquals("Id of method is equal from id expected", idExpected, id);
	}

	@Test
	public void getPathFileTest() {
		String filename = "m-" + date + "-000001.zip";
		String pathFileExpected = this.printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + "01" + File.separator + "m-" + date + "-000001.zip";

		String pathFile = toolService.getPathFile(filename, "m-" + date + "-000001");

		Assert.assertNotNull(pathFile);
		Assert.assertEquals("The path file is equal path file expected", pathFileExpected, pathFile);
	}

	@Test
	public void getPathFileCreateFolderTest() {
		String filename = "m-" + date + "-000042.zip";
		String pathFileExpected = this.printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + "42" + File.separator + "m-" + date + "-000042.zip";

		String pathFile = toolService.getPathFile(filename, "m-" + date + "-000042");

		Assert.assertNotNull(pathFile);
		Assert.assertEquals("The path file is equal path file expected", pathFileExpected, pathFile);
		String pathRepo = this.pathRoot + File.separator + this.printWorldProperties.getRepositoryData() + File.separator + year + File.separator + month + File.separator + day + File.separator + "42";
		File repoForZip = new File(pathRepo);

		if (!repoForZip.exists()) {
			Assert.fail("Test not pass because repository for zip isn't create!");
		}

		toolService.deleteFile(pathRepo);
	}


	private File createFileForTest() {
		File file = new File("data/test.zip");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException ex) {
			Assert.fail("Not create file test!");
		}

		return file;
	}
}
