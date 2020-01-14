package me.tdm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ AppConfig.class })
// @formatter:off
@ComponentScan(basePackages = { "me.tdm" })
// @formatter:on
public class CoreConfig {

}