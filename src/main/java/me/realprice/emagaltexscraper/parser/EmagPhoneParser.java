package me.realprice.emagaltexscraper.parser;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.Vendor;
import me.realprice.emagaltexscraper.dto.Phone;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EmagPhoneParser {

    private final PropertiesComputer propertiesComputer;

    public EmagPhoneParser(PropertiesComputer propertiesComputer) {
        this.propertiesComputer = propertiesComputer;
    }

    public List<Phone> parse(Element document, int page) {

        Elements phoneContainers = document.select(".card-item");
        List<Phone> phones = new ArrayList<>();

        for (Element phoneContainer : phoneContainers) {

            String dataName = phoneContainer.attr("data-name");
            if (dataName.isEmpty()) {
                continue;
            }

            Phone phone = new Phone();
            phone = propertiesComputer.computePhoneProperties(phone, dataName);

            if (phone == null) {
                continue;
            }

            String dataURL = phoneContainer.attr("data-url");
            phone.setUrl(dataURL);

            Element priceElement = phoneContainer.selectFirst(".product-new-price");
            if (priceElement == null) {
                continue;
            }

            String price = priceElement.ownText();

            if (price.isEmpty()) {
                log.warn("No price found, element nr {}, page {}", phoneContainers.indexOf(phoneContainer), page);
                continue;
            }
            phone.setPrice(price);
            phone.setVendor(Vendor.Emag);
            phones.add(phone);
        }
        return phones;
    }
}
