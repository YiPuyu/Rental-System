package com.example.shangting.controller;


import com.example.shangting.dto.ApiResponse;
import com.example.shangting.dto.LoginRequest;
import com.example.shangting.dto.RegisterRequest;
import com.example.shangting.entity.User;
import com.example.shangting.repository.UserRepository;
import com.example.shangting.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.shangting.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;




    @GetMapping("/test")
    public String test() {
        System.out.println("test接口被调用了");
        return "ok";
    }





    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            System.out.println("用户不存在：" + request.getUsername());
            return ApiResponse.fail("用户不存在");
        }

        User user = userOpt.get();

        // 打印输入的密码和数据库密码
        System.out.println("输入密码: " + request.getPassword());
        System.out.println("数据库加密密码: " + user.getPassword());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("密码不匹配！");
            return ApiResponse.fail("密码错误");
        }

        // 生成 JWT
        String token = jwtUtil.generateToken(user.getUsername());

        // 返回 token + role + userId
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("role", user.getRole());      // 🔹 角色
        result.put("userId", String.valueOf(user.getId())); // 🔹 用户ID

        return ApiResponse.success(result);
    }



    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ApiResponse.success(users);
    }
}
