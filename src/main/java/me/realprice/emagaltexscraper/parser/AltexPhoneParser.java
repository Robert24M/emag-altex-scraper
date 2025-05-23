package me.realprice.emagaltexscraper.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.Phone;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AltexPhoneParser {

    @Value("${altex.phone.product.base-url}")
    private String productBaseUrl;
    private final PropertiesComputer propertiesComputer;

    public AltexPhoneParser(PropertiesComputer propertiesComputer) {
        this.propertiesComputer = propertiesComputer;
    }

    public List<Phone> parse(String response) {

        ObjectMapper mapper = new ObjectMapper();
        List<Phone> phones;

        try {
            phones = mapper.readValue(response, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.warn(e.toString());
            throw new IllegalStateException("Couldn't parse the response");
        }

        phones = phones.stream()
                .map(phoneDTO -> {
                    Phone phone = propertiesComputer.computePhoneProperties(phoneDTO, phoneDTO.getName());
                    if (phone != null) {
                        phoneDTO.setUrl(productBaseUrl + phoneDTO.getSku());
                    }
                    return phone;
                })
                .filter(Objects::nonNull)
                .filter(phone -> {
                    if (phone.getStockStatus() != null && phone.getStockStatus().equals("0")) {
                        log.info("Found product out of stock {}", phone);
                        return false;
                    }
                    if (phone.getInBundle() != null && phone.getInBundle().equals("1")) {
                        log.info("Found product in bundle {}", phone);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        return phones;
    }
}
