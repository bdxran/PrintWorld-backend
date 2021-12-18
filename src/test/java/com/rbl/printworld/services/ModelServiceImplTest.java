package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@Import({ModelService.class})
@Slf4j
public class ModelServiceImplTest {

	private Model expectedModel = new Model("m-20211212-000001", "testModel", "blabla",
			"test", "zip", 1, 5, 1564489);
	private PrintWorldProperties printWorldProperties = new PrintWorldProperties("C:\\Users\\rbl\\Documents\\tmp",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data");

	@Autowired
	private ModelService modelService;

	@Test
	public void createModelTest() {
		Model model = new Model("m-20211212-000001", "testModel", "blabla",
				"test", "zip", 1, 5, 1564489);

		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test.zip");


		Model saveModel = modelService.createModel(file.getAbsolutePath(), model);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected is equal", saveModel, expectedModel);
	}
}
