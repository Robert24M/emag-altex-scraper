package me.realprice.emagaltexscraper.services;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.parser.EmagPhoneParser;
import org.hibernate.collection.spi.PersistentBag;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EmagServiceLoader {

    @Value("${emag.phone.base-url}")
    private String baseUrl;

    private final EmagPhoneParser phoneParser;

    public EmagServiceLoader(EmagPhoneParser phoneParser) {
        this.phoneParser = phoneParser;
    }

    public void loadAllPhones() {
        Connection connection = Jsoup.newSession()
                .header("Accept", "")
//				.header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Encoding", "identity")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("cache-control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Priority", "u=0, i")
                .header("Sec-Ch-Ua", "Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Ch-Ua-Platform", "Windows")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
                .header("Upgrade-Insecure-Requests", "1")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                .method(Connection.Method.GET);

        List<PhoneDTO> phoneDTOS = new ArrayList<>();
        boolean hasNextPage = true;
        int page = 1;

        while (hasNextPage) {

            Document document;
            try {
                document = connection.newRequest()
                        .url(String.format(baseUrl, page++))
                        .execute()
                        .parse();
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }

            Element paginator = document.getElementById("listing-paginator");
            if (paginator == null) {
                log.warn("Paginator not found");
                continue;
            }

            Element lastPageElement = paginator.selectFirst("li:last-child");
            if (lastPageElement == null) {
                log.warn("Last page element not found");
                continue;
            }

            Set<String> classes = lastPageElement.classNames();
            if (classes.contains("disabled")) {
                log.info("Reached page {}", page);
                hasNextPage = false;
            }

            Element phonesContainer = document.selectFirst("#card_grid");
            if (phonesContainer == null) {
                log.warn("PhonesContainer not found");
                continue;
            }

            phoneDTOS.addAll(phoneParser.parse(phonesContainer, page));
                    //            EmagPhoneParser.parse(document);
//            Elements elements = document.select(".card-item");
//            if (elements.isEmpty()) {
//                log.info("No data found");
//                return;
//            }
            //  logger.info(elements.getFirst().outerHtml());

//        try {
//            Files.writeString(Path.of("phone.html"), elements.getFirst().outerHtml());
//
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }

        }

        Collections.sort(phoneDTOS);
        phoneDTOS.forEach(phoneDTO -> log.info(phoneDTO.toString()));
    }
}
