package me.realprice.emagaltexscraper.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.Phone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@ConfigurationProperties
public class PropertiesComputer {

    @Getter
    @Setter
    private List<String> brands;

    private static final List<String> UNWANTED_STRINGS = List.of("Telefon", "mobil");
    private static final List<String> IGNORED_PROPERTIES = List.of("dual sim", "5g", "4g");

    private static final int[] RAM_INTERVAL = {1, 24};
    private static final int[] STORAGE_INTERVAL = {32, 512};

    private static final String RAM = "ram";
    private static final String STORAGE = "rom";
    private static final String GB = "gb";
    private static final String TB = "tb";

    //TODO: make some constants/enums to match the properties with
    private static final String NAME_EXAMPLE = "Telefon mobil Samsung Galaxy S24 Ultra, Dual SIM, 12GB RAM, 512GB, 5G, Titanium Black";

    private static final Set<String> BRANDS = new HashSet<>();
    private static final Set<String> MODELS = new HashSet<>();

    private final FileUtils _fileUtils;

    public PropertiesComputer(FileUtils fileUtils) {
        this._fileUtils = fileUtils;
    }
//
//    @PreDestroy
//    public void saveBrandsAndModels() {
//        fileSaver.saveFile("brands.txt", String.join(System.lineSeparator(), BRANDS));
//        fileSaver.saveFile("models.txt", String.join(System.lineSeparator(), MODELS));
//    }

    public Phone computePhoneProperties(Phone phone, String data) {

        if (!data.startsWith("Telefon")) {
            return null;
        }

        for (String unwanted : UNWANTED_STRINGS) {
            data = data.replace(unwanted, "");
        }

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
//        BRANDS.add(brand);

        if (productName.size() < 2) {
            return null;
        }

        String model = WordUtils.capitalizeFully(productName.get(1));
        phoneNameBuilder.append(" ");
        phoneNameBuilder.append(model);
        phone.setModel(model);
//        MODELS.add(model);

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
                phone.setRam(memoryProperty.toUpperCase().trim());
            } else if (memoryProperty.contains(STORAGE)) {
                phone.setStorage(memoryProperty.toUpperCase().trim());
            } else if (memoryProperty.contains(TB)) {
                phone.setStorage(memoryProperty.toUpperCase().trim());
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

    private static int countMatches(Matcher matcher, String joinedSearchStrings) {
        int count = 0;
        while (matcher.find()) {
            // Ensure that only one of the search strings is matched in proximity to a digit
            if (matcher.group().matches(".*" + joinedSearchStrings + ".*")) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {

        Phone phone = new PropertiesComputer(new FileUtils()).computePhoneProperties(new Phone(), NAME_EXAMPLE);
    }
}
