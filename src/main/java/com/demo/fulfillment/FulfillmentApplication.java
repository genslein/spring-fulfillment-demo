package com.demo.fulfillment;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


import java.util.Properties;
import java.util.TimeZone;

@OpenAPIDefinition(
		info = @Info(title = "Fulfillment"),
		externalDocs = @ExternalDocumentation(
				url = "https://github.com/genslein/spring-fulfillment-demo"
		),
		servers = {
				@Server(url = "/")
		}
)
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
		properties.setProperty("springdoc.show-actuator","true");
		properties.setProperty("springdoc.swagger-ui.persistAuthorization", "true");
		properties.setProperty("springdoc.model-converters.deprecating-converter.enabled", "false");

		// https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints-enabling-endpoints
		properties.setProperty("management.endpoint.enabled-by-default", "false");
		properties.setProperty("management.endpoint.info.enabled", "true");
		properties.setProperty("management.endpoint.health.enabled", "true");
		properties.setProperty("management.endpoint.health.show-details", "always");
		properties.setProperty("management.endpoint.metrics.enabled", "true");
		properties.setProperty("management.endpoint.prometheus.enabled", "true");
		properties.setProperty("management.metrics.export.prometheus.enabled", "true");
		properties.setProperty("management.metrics.distribution.percentiles-histogram.http.server.requests", "true");
		properties.setProperty("management.metrics.distribution.percentiles.http.server.requests", "0.5,0.75,0.9,0.95,0.99");
		properties.setProperty("management.endpoints.web.exposure.include", "info,health,prometheus,metrics");

		return properties;
	}
}
