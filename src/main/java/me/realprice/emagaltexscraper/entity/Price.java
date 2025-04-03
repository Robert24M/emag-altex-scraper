package me.realprice.emagaltexscraper.entity;

import jakarta.persistence.*;
import lombok.*;
import me.realprice.emagaltexscraper.Vendor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    @Enumerated(EnumType.STRING)
    private Vendor vendor;
    private String price;

    @Column(name = "date", updatable = false)
    private LocalDateTime dateAdded = LocalDateTime.now();

    public Price(me.realprice.emagaltexscraper.dto.Phone phoneDTO, Phone phone) {
        this.phone = phone;
        this.vendor = phoneDTO.getVendor();
        this.price = phoneDTO.getPrice();
    }
}

