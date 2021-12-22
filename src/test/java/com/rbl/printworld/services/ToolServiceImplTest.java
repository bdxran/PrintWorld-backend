package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RunWith(SpringRunner.class)
@Import({ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ToolServiceImplTest {

	private Model modelExpected = new Model("", "testModel", "blabla",
			"test", "zip", 1, 5, 1564489);
	private PrintWorldProperties printWorldProperties = new PrintWorldProperties("C:\\Users\\rbl\\Documents\\tmp",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\configs\\metaCounter.txt");

	@Autowired
	private ToolServiceImpl toolService;

	//Not work
	@Test
	public void transferMultipartFileToFileTest() throws IOException {
		MultipartFile multipartFile = new MockMultipartFile(
				"name",
				"test.zip",
				MediaType.APPLICATION_OCTET_STREAM_VALUE,
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test.zip")
				);

		String pathFileTmp = toolService.transferMultipartFileToFile(multipartFile, "d-20211222-000001");
		String pathFileTmpExpected = printWorldProperties.getTmp() + File.separator + "d-20211222-000001.zip";

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

		toolService.getExtensionFile(modelExpected, "test.zip");

		Assert.assertNotNull(modelExpected.getExtension());
		Assert.assertEquals("The extension expected is equal from extension return by getExtensionFile", extensionExpected, modelExpected.getExtension());
	}

	@Test
	public void saveFileTest() {
		ToolServiceImpl toolService = new ToolServiceImpl(printWorldProperties);
		File fileSave = new File(printWorldProperties.getRepositoryData() + File.separator + "2021" + File.separator + "12" + File.separator + "22" + File.separator + "01" + File.separator + "d-20211222-000001.zip");
		toolService.saveFile("d-20211222-000001", "C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test-1.zip");

		if (!fileSave.exists()) {
			Assert.fail("Test not pass because file isn't create!");
		}
	}

	@Test
	public void deleteTest() {
		ToolServiceImpl toolService = new ToolServiceImpl(printWorldProperties);
		File fileDelete = new File(printWorldProperties.getRepositoryData() + File.separator + "2021" + File.separator + "12" + File.separator + "22" + File.separator + "01" + File.separator + "d-20211222-000001.zip");
		toolService.deleteFile(fileDelete.getAbsolutePath());

		if (fileDelete.exists()) {
			Assert.fail("Test not pass because file delete exist!");
		}
	}

	@Test
	public void generateIdTest() {
		ToolServiceImpl tool = new ToolServiceImpl(printWorldProperties);
		String idExpected = "m-20211219-000001";
		String id = tool.generateId();

		Assert.assertNotNull(id);
		Assert.assertEquals("Id of method is equal from id expected", idExpected, id);
	}
}
