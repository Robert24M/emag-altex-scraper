package me.realprice.emagaltexscraper.util;

import lombok.extern.slf4j.Slf4j;
import me.realprice.emagaltexscraper.dto.PhoneDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PropertiesComputer {

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

    private final FileSaver fileSaver;

    public PropertiesComputer(FileSaver fileSaver) {
        this.fileSaver = fileSaver;
    }
//
//    @PreDestroy
//    public void saveBrandsAndModels() {
//        fileSaver.saveFile("brands.txt", String.join(System.lineSeparator(), BRANDS));
//        fileSaver.saveFile("models.txt", String.join(System.lineSeparator(), MODELS));
//    }

    public PhoneDTO computePhonePropertiesEmag(PhoneDTO phoneDTO, String data) {

        log.info("Computing for {}", data);

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
        phoneDTO.setBrand(brand);
//        BRANDS.add(brand);

        if (productName.size() < 2) {
            return null;
        }

        String model = WordUtils.capitalizeFully(productName.get(1));
        phoneDTO.setModel(model);
//        MODELS.add(model);

        computeMemoryProperties(phoneDTO, dataComponents);
//        boolean foundRam = false;
//        String ram = null;
//        String storage = "0";
//        for (String dataElement : dataComponents) {
//
//            if (IGNORED_PROPERTIES.contains(dataElement.toLowerCase())) {
//                continue;
//            }
//
//            String digits = StringUtils.getDigits(dataElement);
//            if (!digits.isEmpty()) {
//                if (StringUtils.containsIgnoreCase(dataElement, RAM)) {
//                    foundRam = true;
//                    ram = dataElement;
//                } else if (foundRam) {
//                    storage = dataElement;
//                } else if () {
//
//                }
//            }
//
//
//        }

        return phoneDTO;
    }

    private void computeMemoryProperties(PhoneDTO phoneDTO, List<String> dataComponents) {

        List<String> memoryProperties = dataComponents.stream()
                .filter(component -> StringUtils.containsAny(component, GB, TB, RAM, STORAGE))
                .toList();

        if (memoryProperties.size() > 2) {
            throw new IllegalStateException("Too many memory components");
        }

        if (memoryProperties.isEmpty()) {
            throw new IllegalStateException("No memory components found");
        }

        for (String memoryProperty : memoryProperties) {
            if (memoryProperty.contains(RAM)) {
                phoneDTO.setRam(memoryProperty);
            } else if (memoryProperty.contains(STORAGE)) {
                phoneDTO.setStorage(memoryProperty);
            } else if (memoryProperty.contains(TB)) {
                phoneDTO.setStorage(memoryProperty);
            } else {
                int numericalValue = Integer.parseInt(StringUtils.getDigits(memoryProperty));
                if(numericalValue > RAM_INTERVAL[0] && numericalValue < RAM_INTERVAL[1]) {
                    phoneDTO.setRam(memoryProperty);
                } else if (numericalValue > STORAGE_INTERVAL[0] && numericalValue < STORAGE_INTERVAL[1]) {
                    phoneDTO.setStorage(memoryProperty);
                }
            }
        }
    }

    public static void main(String[] args) {

        PhoneDTO phoneDTO = new PropertiesComputer(new FileSaver()).computePhonePropertiesEmag(new PhoneDTO(), NAME_EXAMPLE);
    }
}
