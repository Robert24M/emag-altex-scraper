package me.realprice.emagaltexscraper;

import me.realprice.emagaltexscraper.services.EmagServiceLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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
