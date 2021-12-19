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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import({ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ToolServiceImplTest {

	private Model modelExpected = new Model("", "testModel", "blabla",
			"test.zip", "zip", 1, 5, 1564489);
	private PrintWorldProperties printWorldProperties = new PrintWorldProperties("C:\\Users\\rbl\\Documents\\tmp",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\configs\\metaCounter.txt");

	@Autowired
	private ToolServiceImpl toolService;

	@Test
	public void transferMultipartFileToFileTest() {
		//TODO
	}

	@Test
	public void getExtensionFileTest() {
		String extensionExpected = "zip";

		toolService.getExtensionFile(modelExpected);

		Assert.assertNotNull(modelExpected.getExtension());
		Assert.assertEquals("The extension expected is equal from extension return by getExtensionFile", extensionExpected, modelExpected.getExtension());
	}

	@Test
	public void saveFileTest() {
		//TODO
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
