package me.realprice.emagaltexscraper.util;


import me.realprice.emagaltexscraper.dto.PhoneDTO;

import java.util.List;

public class PropertiesComputer {

    private static final List<String> UNWANTED_STRINGS = List.of("Telefon mobil");
    //TODO: make some constants/enums to match the properties with
    private static final String NAME_EXAMPLE = "Telefon mobil Samsung Galaxy S24 Ultra, Dual SIM, 12GB RAM, 512GB, 5G, Titanium Black";

    public static void computePhonePropertiesEmag(PhoneDTO phoneDTO, String data) {

        for (String unwanted : UNWANTED_STRINGS) {
            data = data.replace(unwanted, "");
        }

        data = data.replace("\\s+", " ").trim();
        String[] dataComponents = data.split(",");

    }
}
