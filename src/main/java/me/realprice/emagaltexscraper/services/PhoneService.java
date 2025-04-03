package me.realprice.emagaltexscraper.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import me.realprice.emagaltexscraper.entity.Phone;
import me.realprice.emagaltexscraper.entity.Price;
import me.realprice.emagaltexscraper.repository.PhoneRepository;
import me.realprice.emagaltexscraper.repository.PriceRepository;
import me.realprice.emagaltexscraper.util.Utils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final PriceRepository priceRepository;
    private Map<String, Phone> phonesCache;

    public PhoneService(PhoneRepository phoneRepository, PriceRepository priceRepository) {
        this.phoneRepository = phoneRepository;
        this.priceRepository = priceRepository;
    }

    @PostConstruct
    public void loadPhonesInMemory() {
        phonesCache = phoneRepository.findAll().stream()
                .collect(Collectors.toMap(Phone::getNormalizedKey, phone -> phone));
    }

    @Transactional
    public void addPhoneWithPrice(me.realprice.emagaltexscraper.dto.Phone phoneDTO) {
        String key = Utils.getNormalizeKey(phoneDTO.getBrand(), phoneDTO.getModel(), phoneDTO.getRam(), phoneDTO.getStorage(), phoneDTO.getColor());
        Phone phone = phonesCache.get(key);

        if (phone == null) {
            try {
                phone = phoneRepository.save(new Phone(phoneDTO));
                phonesCache.put(key, phone);
            } catch (DataIntegrityViolationException e) {
                phone = phoneRepository.findByNormalizedKey(key).orElseThrow();
            }
        }

        Price priceEntry = new Price(phoneDTO, phone);
        priceRepository.save(priceEntry);
    }
}
