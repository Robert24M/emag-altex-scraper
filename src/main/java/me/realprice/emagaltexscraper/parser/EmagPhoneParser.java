package me.realprice.emagaltexscraper.parser;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.util.FileSaver;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class EmagPhoneParser {

    private final FileSaver fileSaver;

    public EmagPhoneParser(FileSaver fileSaver) {
        this.fileSaver = fileSaver;
    }

    public List<PhoneDTO> parse(Element document) {

        Elements phoneContainers = document.select(".card-item");
        List<PhoneDTO> phones = new ArrayList<>();

        List<String> dataNames = new ArrayList<>(); // for test
        for (Element phoneContainer : phoneContainers) {

            PhoneDTO phone = new PhoneDTO();
//            Element dataNameElement = phoneContainer.selectFirst(".data-name");
//            if (dataNameElement == null) {
//                logger.warn("No data-name in element nr {}", phoneContainers.indexOf(phoneContainer));
//                continue;
//            }
            String dataName = phoneContainer.attr("data-name");
            if (dataName.isEmpty()) {
                log.warn("No text in dataName, element nr {}", phoneContainers.indexOf(phoneContainer));
                continue;
            }

            dataNames.add(dataName);
            // need to compute fields to match PhoneDTO fields
            PropertiesComputer.computePhonePropertiesEmag(phone, dataName);

            String dataURL = phoneContainer.attr("data-url");
            if (dataURL.isEmpty()) {
                log.warn("No url, element nr {}", phoneContainers.indexOf(phoneContainer));
            }
            phone.setUrl(dataURL);

            Element priceElement = phoneContainer.selectFirst(".product-new-price");
            if (priceElement == null) {
                log.warn("No price element, element nr {}", phoneContainers.indexOf(phoneContainer));
                continue;
            }

            String price = priceElement.ownText();

            if (price.isEmpty()) {
                log.warn("No price found, element nr {}", phoneContainers.indexOf(phoneContainer));
            }
            phone.setPrice(Double.parseDouble(price));

            phones.add(phone);
        }
//        Collections.sort(phones);
        fileSaver.saveFile("data-names.txt", String.join(System.lineSeparator(), dataNames));
        return phones;
    }
}
