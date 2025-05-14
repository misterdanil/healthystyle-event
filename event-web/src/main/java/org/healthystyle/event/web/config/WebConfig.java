package org.healthystyle.event.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("http://localhost:3000", "http://localhost:3002").allowCredentials(true)
				.exposedHeaders("Location", "Authorization");
	}
}