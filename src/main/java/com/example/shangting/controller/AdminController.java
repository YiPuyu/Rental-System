package com.example.shangting.controller;

import com.example.shangting.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



import com.example.shangting.entity.User;
import com.example.shangting.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 仅管理员可以访问
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "用户已删除";
    }
}


