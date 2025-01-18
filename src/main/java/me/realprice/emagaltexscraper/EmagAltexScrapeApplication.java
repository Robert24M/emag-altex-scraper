package me.realprice.emagaltexscraper;

import me.realprice.emagaltexscraper.services.EmagServiceLoader;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@SpringBootApplication
@EnableConfigurationProperties
public class EmagAltexScrapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmagAltexScrapeApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(EmagServiceLoader loader) {
		return runner -> {
			loader.loadAllPhones();
		};
	}
}
