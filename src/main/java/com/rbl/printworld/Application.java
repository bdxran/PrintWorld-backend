package com.rbl.printworld;

import com.rbl.printworld.models.PrintWorldProperties;
import com.rbl.printworld.services.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rbl.printworld.repositories")
@Slf4j
public class Application implements CommandLineRunner {

	static ConfigurableApplicationContext appCtx;
	private final PrintWorldProperties properties;
	//TODO Faire en sorte que le metaCounter soit sauver à la fermeture de l'application
	//TODO Ajouter une vérification que toutes les properties soient disponibles

	@Autowired
	public Application(PrintWorldProperties properties) {
		this.properties = properties;
	}

	public static void main(String[] args) {
		appCtx = SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("------------------------");
		log.info("Application PrintWorld is run with configuration :");
		log.info("------------------------");
		log.info("Repository for tmp is : " + properties.getTmp());
		log.info("Repository for data is : " + properties.getRepositoryData());
		log.info("File metaCounter is : " + properties.getMetaCounter());
	}

	public static void shutdown() {
		if (appCtx.isActive()) {
			appCtx.close();
		}
	}

	public static void restart() {
		var args = appCtx.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			appCtx.close();
			appCtx = SpringApplication.run(Application.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}
}
