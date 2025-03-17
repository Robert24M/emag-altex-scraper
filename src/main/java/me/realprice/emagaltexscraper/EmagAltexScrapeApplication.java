package me.realprice.emagaltexscraper;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.Phone;
import me.realprice.emagaltexscraper.services.AltexServiceLoader;
import me.realprice.emagaltexscraper.services.EmagServiceLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.stream.Collectors;

import static me.realprice.emagaltexscraper.Vendor.Altex;
import static me.realprice.emagaltexscraper.Vendor.Emag;

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
    public CommandLineRunner commandLineRunner(EmagServiceLoader emagServiceLoader, AltexServiceLoader altexServiceLoader) {
        return runner -> {
            Map<Phone, String> emagPhones = emagServiceLoader.loadAllPhones().stream()
                    .collect(Collectors.toMap(phone -> phone, Phone::getPrice));
            Map<Phone, String> altexPhones = altexServiceLoader.loadAllPhones().stream()
                    .collect(Collectors.toMap(phone -> phone, Phone::getPrice));

            Map<Phone, List<String>> commonPhones = new HashMap<>();
            for (Map.Entry<Phone, String> phone : altexPhones.entrySet()) {
                String emagPrice = emagPhones.get(phone.getKey());
                if (emagPrice != null) {
                    commonPhones.put(phone.getKey(), List.of(Altex + ":" + phone.getValue(), Emag + ":" + emagPrice));
                }
            }

            commonPhones.forEach((phone, strings) -> log.info("{}:{}", phone, strings));
        };
    }
}
