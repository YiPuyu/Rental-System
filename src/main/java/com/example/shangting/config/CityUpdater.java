package com.example.shangting.config;

import com.example.shangting.repository.PropertyRepository;
import com.example.shangting.entity.Property;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CityUpdater implements CommandLineRunner {

    private final PropertyRepository propertyRepository;

    public CityUpdater(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        propertyRepository.findAll().forEach(p -> {
            String addr = p.getAddress(); // 假设地址是 "东京 区 12 号"
            if (addr.contains("东京")) p.setCity("Tokyo");
            else if (addr.contains("大阪")) p.setCity("Osaka");
            else if (addr.contains("横滨")) p.setCity("Yokohama");
            else if (addr.contains("札幌")) p.setCity("Sapporo");
            else if (addr.contains("名古屋")) p.setCity("Nagoya");

            propertyRepository.save(p);
        });

        System.out.println("城市字段已更新为英文");
    }
}
