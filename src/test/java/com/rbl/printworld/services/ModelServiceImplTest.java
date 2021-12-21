package com.rbl.printworld.services;

import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ModelServiceImpl;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@Import({})
@Slf4j
public class ModelServiceImplTest {

	private Model expectedModel = new Model("", "testModel", "blabla",
			"test.zip", "zip", 1, 5, 1564489);
	private PrintWorldProperties printWorldProperties = new PrintWorldProperties("C:\\Users\\rbl\\Documents\\tmp",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data",
			"C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\configs\\metaCounter.txt");

	@Test
	public void createModelTest() {
		ToolServiceImpl toolService = new ToolServiceImpl(printWorldProperties);
		//TODO use mock
		ModelServiceImpl modelService = new ModelServiceImpl(toolService, null, printWorldProperties);
		String id = toolService.generateId();

		Model model = new Model(id, "testModel", "blabla",
				"test", "zip", 1, 5, 1564489);

		File file = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld\\data\\test.zip");

		Model saveModel = modelService.createModel(id, file.getAbsolutePath(), model);

		Assert.assertNotNull(saveModel);
		Assert.assertEquals("Model save and model expected is equal", saveModel, this.expectedModel);
	}
}
