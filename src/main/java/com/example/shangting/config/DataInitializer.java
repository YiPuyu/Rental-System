package com.example.shangting.config;

import com.example.shangting.entity.Property;
import com.example.shangting.repository.PropertyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final String[] cities = {"tokyo", "osaka", "yokohama", "sapporo", "nagoya"};
    private final String[] titles = {"豪华公寓", "精致小屋", "海景房", "温馨公寓", "现代公寓"};
    private final String[] descriptions = {
            "交通便利，生活方便",
            "环境优美，安静舒适",
            "风景绝佳，阳光充足",
            "家具齐全，即刻入住",
            "性价比高，房东热情"
    };
    private final Random random = new Random();


    public DataInitializer(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }



    @Override
    public void run(String... args) throws Exception {
        if(propertyRepository.count() > 0) {
            System.out.println("数据库已有房源，跳过生成虚拟数据");
            return;
        }

        for(int i = 1; i <= 50; i++) { // 生成 50 条房源
            Property p = new Property();
            p.setTitle(titles[random.nextInt(titles.length)] + " " + i);
            p.setAddress(cities[random.nextInt(cities.length)] + " 区 " + (random.nextInt(100) + 1) + " 号");
            p.setRent(500 + random.nextInt(2000)); // 租金 500~2500
            p.setOwnerId((long) (random.nextInt(3) + 1)); // 3 个房东循环分配
            p.setDescription(descriptions[random.nextInt(descriptions.length)]);

            propertyRepository.save(p);
        }

        System.out.println("已生成 50 条虚拟房源数据");
    }
}

