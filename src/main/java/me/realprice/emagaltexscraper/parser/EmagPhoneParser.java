package me.realprice.emagaltexscraper.parser;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.Vendor;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.util.FileUtils;
import me.realprice.emagaltexscraper.util.PropertiesComputer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EmagPhoneParser {

    private final FileUtils _fileUtils;
    private final PropertiesComputer propertiesComputer;

    public EmagPhoneParser(FileUtils fileUtils, PropertiesComputer propertiesComputer) {
        this._fileUtils = fileUtils;
        this.propertiesComputer = propertiesComputer;
    }

    public List<PhoneDTO> parse(Element document, int page) {

        Elements phoneContainers = document.select(".card-item");
        List<PhoneDTO> phones = new ArrayList<>();

        List<String> dataNames = new ArrayList<>(); // for test
        for (Element phoneContainer : phoneContainers) {

            String dataName = phoneContainer.attr("data-name");
            if (dataName.isEmpty()) {
//                log.warn("No text in dataName, element nr {}, page {}", phoneContainers.indexOf(phoneContainer), page);
//                fileSaver.saveFile( "element" + phoneContainers.indexOf(phoneContainer) + "_page" + page, phoneContainer.outerHtml());
                continue;
            }

            dataNames.add(dataName);
            // need to compute fields to match PhoneDTO fields
            PhoneDTO phone = new PhoneDTO();
            phone = propertiesComputer.computePhonePropertiesEmag(phone, dataName);

            if (phone == null) {
//                log.warn("Could not compute phone properties, element nr {}, page {}, dataName{}", phoneContainers.indexOf(phoneContainer),page, dataName);
//                fileSaver.saveFile( "element" + phoneContainers.indexOf(phoneContainer) + "_page" + page, phoneContainer.outerHtml());
                continue;
            }

            String dataURL = phoneContainer.attr("data-url");
            if (dataURL.isEmpty()) {
//                log.warn("No url, element nr {}, page {}", phoneContainers.indexOf(phoneContainer), page);
//                fileSaver.saveFile( "element" + phoneContainers.indexOf(phoneContainer) + "_page" + page, phoneContainer.outerHtml());
            }
            phone.setUrl(dataURL);

            Element priceElement = phoneContainer.selectFirst(".product-new-price");
            if (priceElement == null) {
//                log.warn("No price element, element nr {}, page {}", phoneContainers.indexOf(phoneContainer), page);
//                fileSaver.saveFile( "element" + phoneContainers.indexOf(phoneContainer) + "_page" + page, phoneContainer.outerHtml());
                continue;
            }

            String price = priceElement.ownText();

            if (price.isEmpty()) {
                log.warn("No price found, element nr {}, page {}", phoneContainers.indexOf(phoneContainer), page);
                continue;
//                fileSaver.saveFile( "element" + phoneContainers.indexOf(phoneContainer) + "_page" + page, phoneContainer.outerHtml());
            }
            phone.setPrice(Double.parseDouble(price));
            phone.setVendor(Vendor.Emag);

            String fileName = phone.getName().replace(" ", "_")
                    .replace("\\", "")
                    .replace("/", "")
                    .replace("\"", "")
                    .replace(":", "");
            _fileUtils.saveFile(fileName + ".html", phoneContainer.outerHtml());
            phones.add(phone);
        }

        _fileUtils.saveFile("data-names.txt", String.join(System.lineSeparator(), dataNames));
        return phones;
    }
}
