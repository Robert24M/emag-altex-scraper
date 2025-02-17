package me.realprice.emagaltexscraper;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.parser.AltexPhoneParser;
import me.realprice.emagaltexscraper.services.AltexServiceLoader;
import me.realprice.emagaltexscraper.services.EmagServiceLoader;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.hibernate.dialect.PgJdbcHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class EmagAltexScrapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmagAltexScrapeApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(EmagServiceLoader emagServiceLoader, AltexServiceLoader altexServiceLoader) {
		return runner -> {
//			List<PhoneDTO> emagPhones = emagServiceLoader.loadAllPhones();
			List<PhoneDTO> altexPhones = altexServiceLoader.loadAllPhones();
			altexPhones.forEach(System.out::println);

//            List<PhoneDTO> allPhones = new ArrayList<>();
//			allPhones.addAll(altexPhones);

			//todo: see when data from altex is null and treat that cases
//			Collections.sort(allPhones);
//			allPhones.forEach(phoneDTO -> log.info(phoneDTO.toString()));
		};
	}
}
