package me.realprice.emagaltexscraper.services;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.parser.EmagPhoneParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class EmagService {

    @Value("${emag.phone.base-url}")
    private String baseUrl;

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

        Element element = null;

        try {
            element = connection.newRequest()
                    .url(String.format(baseUrl, 1))
                    .execute()
                    .parse()
                    .selectFirst("#card_grid");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Elements elements = null;
        if (element != null) {
            EmagPhoneParser.parse(element);
            elements = element.select(".card-item");
        }
        if (elements == null || elements.isEmpty()) {
            log.info("No data found");
            return;
        }
        //  logger.info(elements.getFirst().outerHtml());

        try {
            Files.writeString(Path.of("phone.html"), elements.getFirst().outerHtml());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}
