package me.tdm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;



@Configuration
@PropertySource({ "classpath:database.properties", "classpath:application.properties", "classpath:log4j.properties" })
@Import({ RepositoryConfig.class })
@ComponentScan(basePackages = { "me.tdm.entry", "me.tdm.dao", "me.tdm.logic" })
public class TestConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
