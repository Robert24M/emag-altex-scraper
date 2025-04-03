package me.realprice.emagaltexscraper.repository;

import me.realprice.emagaltexscraper.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findByNormalizedKey(String normalizedKey);
}
