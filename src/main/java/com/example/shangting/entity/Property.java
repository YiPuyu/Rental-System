package com.example.shangting.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private Double price;

    private String address;
    private int rent;

    // 新增筛选字段
    private String city;       // 城市（例如 Boston, New York）
    private String houseType;  // 户型（例如 Apartment, Studio, House）

    // 房东id
    private Long ownerId;

    public Property() {}

    public Property(Long id, String title, String address, int rent) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.rent = rent;
    }

    // 你也可以加上一个全字段构造函数
    public Property(Long id, String title, String description, Double price,
                    String address, int rent, String city, String houseType, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.address = address;
        this.rent = rent;
        this.city = city;
        this.houseType = houseType;
        this.ownerId = ownerId;
    }
}
