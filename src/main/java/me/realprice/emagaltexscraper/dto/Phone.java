package me.realprice.emagaltexscraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.realprice.emagaltexscraper.Vendor;
import org.jsoup.nodes.Element;

import java.util.Objects;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Phone implements Comparable<Phone> {

    @JsonProperty("name")
    private String name;
    @JsonProperty("brand_name")
    private String brand;
    private String model;
    private String ram;
    private String storage;
    private String color;
    @JsonProperty("price")
    private String price;
    @JsonProperty("url_key")
    private String url;
    private Vendor vendor = Vendor.Altex;    // set default to Altex, override on eMag, TODO: find better option
    private Element source;
    @JsonProperty("stock_status")
    private String stockStatus;

    @Override
    public int compareTo(Phone o) {

        int brandComparator = brand.compareToIgnoreCase(o.brand);
        if (brandComparator == 0) {
            return model.compareTo(o.model);
        }

        return brandComparator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;

        return equalsIgnoreCaseAndIgnoreSpaces(this.brand, phone.brand) &&
                equalsIgnoreCaseAndIgnoreSpaces(this.model, phone.model) &&
                equalsIgnoreCaseAndIgnoreSpaces(this.ram, phone.ram) &&
                equalsIgnoreCaseAndIgnoreSpaces(this.color, phone.color);
    }

    private boolean equalsIgnoreCaseAndIgnoreSpaces(String s1, String s2) {
        return normalize(s1).equals(normalize(s2));
    }

    private String normalize(String s) {
        return (s == null) ? "" : s.replaceAll("\\s+", "").toLowerCase();
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, ram, storage);
    }
}
