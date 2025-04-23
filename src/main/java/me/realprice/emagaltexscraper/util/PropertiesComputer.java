package me.realprice.emagaltexscraper.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.Phone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@Service
@Slf4j
@ConfigurationProperties
public class PropertiesComputer {

    private List<String> brands;

    private static final List<String> UNWANTED_STRINGS = List.of("Telefon", "mobil", "(product)", "dual", "sim", "5g", "4g");

    private static final int[] RAM_INTERVAL = {1, 24};
    private static final int[] STORAGE_INTERVAL = {32, 512};

    private static final String RAM = "ram";
    private static final String STORAGE = "rom";
    private static final String GB = "gb";
    private static final String TB = "tb";

    public Phone computePhoneProperties(Phone phone, String data) {

        if (!data.startsWith("Telefon")) {
            return null;
        }

        String pattern = "\\b(?i)(" + String.join("|", UNWANTED_STRINGS) + ")\\b";
        data = data.replaceAll(pattern, "");

        data = data.replace("\\s+", " ").trim().toLowerCase();
        List<String> dataComponents = List.of(data.split(","));

        List<String> productName = List.of(dataComponents.getFirst().split("\\s", 2));

        String brand = StringUtils.capitalize(productName.getFirst());

        if (!brands.contains(brand)) {
            return null;
        }

        StringBuilder phoneNameBuilder = new StringBuilder();
        phoneNameBuilder.append(brand);

        phone.setBrand(brand);

        if (productName.size() < 2) {
            return null;
        }

        String model = WordUtils.capitalizeFully(productName.get(1)).trim();
        phoneNameBuilder.append(" ");
        phoneNameBuilder.append(model);
        phone.setModel(model);

        computeMemoryProperties(phone, dataComponents);

        if (phone.getRam() != null) {
            phoneNameBuilder.append(" ")
                    .append(phone.getRam());
        }
        if (phone.getStorage() != null) {
            phoneNameBuilder.append(" ")
                    .append(phone.getStorage());
        }

        String color = WordUtils.capitalizeFully(dataComponents.getLast()).trim();
        phone.setColor(color);
        phoneNameBuilder.append(" ")
                .append(color);

        phone.setName(phoneNameBuilder.toString());
        return phone;
    }

    private void computeMemoryProperties(Phone phone, List<String> dataComponents) {

        String searchString = String.join("|", GB, TB, RAM, STORAGE);
        String pattern = "(?<=\\d\\s?)(" + searchString + ")\\b|\\b(" + searchString + ")(?=\\s?\\d)";
        List<String> memoryProperties = dataComponents.stream()
                .filter(component -> {
                    Matcher matcher = Pattern.compile(pattern).matcher(component);
                    return matcher.find();
                }).toList();

        if (memoryProperties.size() > 2) {
            log.warn("Too many memory components, {}", dataComponents);
            return;
        }

        if (memoryProperties.isEmpty()) {
            log.warn("No memory components found, {}", dataComponents);
            return;
        }

        for (String memoryProperty : memoryProperties) {
            if (memoryProperty.contains(RAM)) {
                phone.setRam(memoryProperty.replace(RAM, "").toUpperCase().trim());
            } else if (memoryProperty.contains(STORAGE)) {
                phone.setStorage(memoryProperty.replace(STORAGE, "").toUpperCase().trim());
            } else if (memoryProperty.contains(TB)) {
                phone.setStorage(memoryProperty.replace(TB, "").toUpperCase().trim());
            } else {
                long numericalValue = Long.parseLong(StringUtils.getDigits(memoryProperty));
                if (numericalValue > RAM_INTERVAL[0] && numericalValue < RAM_INTERVAL[1]) {
                    phone.setRam(memoryProperty.toUpperCase().trim());
                } else if (numericalValue > STORAGE_INTERVAL[0] && numericalValue < STORAGE_INTERVAL[1]) {
                    phone.setStorage(memoryProperty.toUpperCase().trim());
                }
            }
        }
    }
}
