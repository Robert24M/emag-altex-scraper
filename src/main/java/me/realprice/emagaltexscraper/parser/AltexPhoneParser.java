package me.realprice.emagaltexscraper.parser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AltexPhoneParser {

    private static final Logger logger = LoggerFactory.getLogger(AltexPhoneParser.class.getName());

    public static List<PhoneDTO> parse(String response) {

        ObjectMapper mapper = new ObjectMapper();
        List<PhoneDTO> phones;

        try {
            phones = mapper.readValue(response, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.warn(e.toString());
            throw new IllegalStateException("Couldn't parse the response");
        }

         Collections.sort(phones);
        return phones;
    }
}
