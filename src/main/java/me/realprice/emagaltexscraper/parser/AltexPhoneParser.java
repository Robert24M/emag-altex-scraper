package me.realprice.emagaltexscraper.parser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AltexPhoneParser {

    public static List<PhoneDTO> parse(String response) {

        ObjectMapper mapper = new ObjectMapper();
        List<PhoneDTO> phones;

        try {
            phones = mapper.readValue(response, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.warn(e.toString());
            throw new IllegalStateException("Couldn't parse the response");
        }

         Collections.sort(phones);
        return phones;
    }
}
