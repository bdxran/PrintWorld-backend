package com.rbl.printworld;

import com.rbl.printworld.models.PrintWorldProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@Slf4j
@IntegrationComponentScan
public class Application implements CommandLineRunner {

	private final PrintWorldProperties properties;

	@Autowired
	public Application(PrintWorldProperties properties) {
		this.properties = properties;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("------------------------");
		log.info("Application PrintWorld is run with configuration :");
		log.info("------------------------");
		log.info("Repository for tmp is : " + properties.getTmp());
		log.info("Repository for data is : " + properties.getRepertoryData());
	}

}
