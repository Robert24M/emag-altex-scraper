package me.realprice.emagaltexscraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.realprice.emagaltexscraper.Vendor;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
