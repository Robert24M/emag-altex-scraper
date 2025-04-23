package me.realprice.emagaltexscraper;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.Phone;
import me.realprice.emagaltexscraper.services.AltexServiceLoader;
import me.realprice.emagaltexscraper.services.EmagServiceLoader;
import me.realprice.emagaltexscraper.services.PhoneService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class EmagAltexScrapeApplication {

    // TODO: Go with a basic form and put the project on a server
    // TODO: Improve using NLP
    public static void main(String[] args) {
        SpringApplication.run(EmagAltexScrapeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EmagServiceLoader emagServiceLoader, AltexServiceLoader altexServiceLoader, PhoneService phoneService) {
        return runner -> {
            List<Phone> phones = new ArrayList<>(emagServiceLoader.loadAllPhones());
            phones.addAll(altexServiceLoader.loadAllPhones());

            for (Phone phone : phones) {
                phoneService.addPhoneWithPrice(phone);
            }
        };
    }
}
