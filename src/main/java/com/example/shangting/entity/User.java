
    package com.example.shangting.entity;

import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Data
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(nullable = false)
        private String password;

        private String email;

        private String role; // tenant / landlord / admin
    }


