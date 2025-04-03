package me.realprice.emagaltexscraper.repository;

import me.realprice.emagaltexscraper.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
