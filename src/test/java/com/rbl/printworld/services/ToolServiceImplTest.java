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
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ToolServiceImplTest {

	private String pattern = "yyyyMMdd";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	private String date = simpleDateFormat.format(new Date());
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

		String pathFileTmp = toolService.transferMultipartFileToFileTmp(multipartFile, "m-" + date + "-000001");
		String pathFileTmpExpected = printWorldProperties.getTmp() + File.separator + "tmp_m-" + date + "-000001.zip";

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
	public void generateIdTest() {
		String idExpected = "m-" + date + "-000001";
		String id = toolService.generateId();

		Assert.assertNotNull(id);
		Assert.assertEquals("Id of method is equal from id expected", idExpected, id);
	}

	@Test
	public void getPathFileTest() {
		String filename = "m-20211224-000001.zip";
		String pathFileExpected = "C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\2021\\12\\24\\01\\m-20211224-000001.zip";

		String pathFile = toolService.getPathFile(filename, "m-" + date + "-000001");

		Assert.assertNotNull(pathFile);
		Assert.assertEquals("The path file is equal path file expected", pathFileExpected, pathFile);
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
