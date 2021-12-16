package com.rbl.printworld.configurations;

import com.rbl.printworld.models.PrintWorldProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class PrintWorldConfig {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "printworld")
	public PrintWorldProperties getCustomerProperties() {
		return new PrintWorldProperties();
	}
}
