package me.realprice.emagaltexscraper.parser;

import me.realprice.emagaltexscraper.dto.PhoneDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EmagPhoneParser {

    private static final Logger logger = LoggerFactory.getLogger(AltexPhoneParser.class.getName());

    public static List<PhoneDTO> parse(Document document) {

        Elements phoneContainers = document.select(".card-item");
        List<PhoneDTO> phoneDTOs = new ArrayList<>();
        for (Element phoneContainer : phoneContainers) {

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

            String[] dataNameParts = dataName.split(",");
            // need to compute fields to match PhoneDTO fields

            String dataURL = phoneContainer.attr("data-url");
            if (dataURL.isEmpty()) {
                logger.warn("No url, element nr {}", phoneContainers.indexOf(phoneContainer));
            }

            Element priceElement = phoneContainer.selectFirst(".product-new-price");
            if (priceElement == null) {
                logger.warn("No price element for, element nr {}", phoneContainers.indexOf(phoneContainer));
                continue;
            }

            String price = priceElement.ownText();
        }
        return phoneDTOs;
    }
}
