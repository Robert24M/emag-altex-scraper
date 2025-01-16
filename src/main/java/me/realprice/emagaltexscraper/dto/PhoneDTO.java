package me.realprice.emagaltexscraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.realprice.emagaltexscraper.Vendor;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneDTO implements Comparable<PhoneDTO>{

    @JsonProperty("name")
    private String name;
    @JsonProperty("brand_name")
    private String brand;
    private String model;
    private String ram;
    private String storage;
    private String color;
    @JsonProperty("price")
    private double price;
    @JsonProperty("url_key")
    private String url;
    private Vendor vendor;

    @Override
    public int compareTo(PhoneDTO o) {

        int brandComparator = brand.compareTo(o.brand);
        if (brandComparator == 0) {
            return model.compareTo(o.model);
        }

        return brandComparator;
    }

}
