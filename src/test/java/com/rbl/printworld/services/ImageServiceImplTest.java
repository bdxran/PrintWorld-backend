package com.rbl.printworld.services;

import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.impl.ImageServiceImpl;
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
@Import({ImageServiceImpl.class, ToolServiceImpl.class, PrintWorldProperties.class})
@Slf4j
public class ImageServiceImplTest {

	@Autowired
	private ImageServiceImpl imageService;
	@Autowired
	private ToolServiceImpl toolService;

	private final PrintWorldProperties printWorldProperties = PrintWorldProperties.builder()
			.tmp("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\tmp")
			.repositoryData("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data")
			.metaCounter("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\configs\\metaCounter.txt")
			.build();

	@Test
	public void uploadImageTest() throws IOException {
		File imageTest = new File("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.png");
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
				new FileInputStream("C:\\Users\\rbl\\Documents\\Projets\\TFE\\PrintWorld-backend\\data\\test.png")
		);

		String imageTmpTest = imageService.uploadImage(multipartFile);

		Assert.assertNotNull("Path tmp image is null!", imageTmpTest);

		File fileTest = new File(imageTmpTest);
		if (!fileTest.exists()) {
			Assert.fail("Test not pass because image " + imageTmpTest + " isn't create!");
		}
	}
}
