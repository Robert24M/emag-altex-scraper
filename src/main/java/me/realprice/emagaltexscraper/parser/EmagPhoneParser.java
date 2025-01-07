package me.realprice.emagaltexscraper.parser;

import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EmagPhoneParser {

    private static final Logger logger = LoggerFactory.getLogger(AltexPhoneParser.class.getName());

    public static List<PhoneDTO> parse(Element document) {

        Elements phoneContainers = document.select(".card-item");
        List<PhoneDTO> phones = new ArrayList<>();
        for (Element phoneContainer : phoneContainers) {

            PhoneDTO phone = new PhoneDTO();
//            Element dataNameElement = phoneContainer.selectFirst(".data-name");
//            if (dataNameElement == null) {
//                logger.warn("No data-name in element nr {}", phoneContainers.indexOf(phoneContainer));
//                continue;
//            }
            String dataName = phoneContainer.attr("data-name");
            if (dataName.isEmpty()) {
                logger.warn("No text in dataName, element nr {}", phoneContainers.indexOf(phoneContainer));
                continue;
            }
            // need to compute fields to match PhoneDTO fields
            PropertiesComputer.computePhonePropertiesEmag(phone, dataName);

            String dataURL = phoneContainer.attr("data-url");
            if (dataURL.isEmpty()) {
                logger.warn("No url, element nr {}", phoneContainers.indexOf(phoneContainer));
            }
            phone.setUrl(dataURL);

            Element priceElement = phoneContainer.selectFirst(".product-new-price");
            if (priceElement == null) {
                logger.warn("No price element, element nr {}", phoneContainers.indexOf(phoneContainer));
                continue;
            }

            String price = priceElement.ownText();

            if (price.isEmpty()) {
                logger.warn("No price found, element nr {}", phoneContainers.indexOf(phoneContainer));
            }
            phone.setPrice(Double.parseDouble(price));

            phones.add(phone);
        }
        Collections.sort(phones);
        return phones;
    }
}
