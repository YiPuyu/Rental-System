package com.example.shangting.controller;

import com.example.shangting.dto.RegisterRequest;
import com.example.shangting.entity.User;
import com.example.shangting.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "用户名已存在"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        // 设置角色，默认是 TENANT，如果前端提交了有效角色，则使用前端提交
        String role = request.getRole();
        if (role == null) {
            role = "TENANT";
        } else {
            role = role.toUpperCase();
            if (!List.of("TENANT", "LANDLORD", "ADMIN").contains(role)) {
                role = "TENANT";
            }
        }
        user.setRole(role);
        User savedUser = userRepository.save(user); // 🔹 这里真正保存到数据库
        System.out.println("已保存用户：" + savedUser);




        return ResponseEntity.ok(Map.of("message", "注册成功"));
    }
}

