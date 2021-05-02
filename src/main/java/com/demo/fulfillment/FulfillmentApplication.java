package com.demo.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.TimeZone;

@SpringBootApplication
public class FulfillmentApplication {

	public static void main(String[] args) {
		// Original preconfigured from Initialzr
		// SpringApplication.run(FulfillmentApplication.class, args);
		new SpringApplicationBuilder(FulfillmentApplication.class)
			.properties(props())
			.build()
			.run(args);
	}

	@PostConstruct
	public void postInit(){
		// Make Application always use UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	// https://stackoverflow.com/a/59016834
	private static Properties props() {
		Properties properties = new Properties();
		// properties.setProperty("logging.level.root", "debug");
		return properties;
	}
}
