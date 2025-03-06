package me.realprice.emagaltexscraper.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import me.realprice.emagaltexscraper.parser.AltexPhoneParser;
import me.realprice.emagaltexscraper.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
public class AltexServiceLoader {

    @Value("${altex.phone.base-url}")
    private String baseUrl;
    private final FileUtils fileUtils;
    private final AltexPhoneParser phoneParser;

    public AltexServiceLoader(FileUtils fileUtils, AltexPhoneParser phoneParser) {
        this.fileUtils = fileUtils;
        this.phoneParser = phoneParser;
    }

    public List<PhoneDTO> loadAllPhones() {

        HttpResponse<byte[]> response;
        List<PhoneDTO> phones = null;
        try (HttpClient client = HttpClient.newHttpClient()) {

            int page = 1;

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
//                    .header("Accept-Encoding", "gzip, deflate, br, zstd")
                    .header("Accept-Encoding", "identity")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Cache-Control", "max-age=0")
                    .header("Priority", "u=0, i")
                    .header("Sec-Ch-Ua", "Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "Windows")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "none")
                    .header("Sec-Fetch-User", "?1")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .GET();

            while (page < 20) {
                HttpRequest request = requestBuilder
                        .uri(new URI(String.format(baseUrl, page)))
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

                String responseBody = new String(response.body());
                JsonMapper jsonMapper = new JsonMapper();
                JsonNode root = jsonMapper.readTree(responseBody);
                JsonNode products = root.get("products");

                if (products == null) {
                    break;
                }
                String pruductsPrettyString = products.toPrettyString();
                fileUtils.saveFile("altexProducts.json", pruductsPrettyString);

                phones = phoneParser.parse(pruductsPrettyString);
                page++;
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return phones;
    }
}
