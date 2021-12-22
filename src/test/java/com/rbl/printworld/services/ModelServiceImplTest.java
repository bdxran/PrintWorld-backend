package com.rbl.printworld.services;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClients;
import com.rbl.printworld.models.Model;
import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ModelServiceImpl;
import com.rbl.printworld.services.impl.ToolServiceImpl;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
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
		DBObject objectToSave = BasicDBObjectBuilder.start()
				.add("key", "value")
				.get();

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
