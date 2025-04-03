package me.realprice.emagaltexscraper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.realprice.emagaltexscraper.util.Utils;

@Entity
@Table(name = "phones", uniqueConstraints = @UniqueConstraint(columnNames = {"normalized_key"})
)
@Getter
@Setter
@NoArgsConstructor
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String ram;
    private String storage;
    private String color;

    @Column(nullable = false, unique = true)
    private String normalizedKey;

    public Phone(me.realprice.emagaltexscraper.dto.Phone phoneDTO) {
        this.brand = phoneDTO.getBrand();
        this.model = phoneDTO.getModel();
        this.ram = phoneDTO.getRam();
        this.storage = phoneDTO.getStorage();
        this.color = phoneDTO.getColor();
    }

    @PrePersist
    @PreUpdate
    private void normalizeKey() {
        this.normalizedKey = Utils.getNormalizeKey(brand, model, ram, storage, color);
    }
}
