package com.example.shangting.controller;

import com.example.shangting.dto.ApiResponse;
import com.example.shangting.entity.Property;
import com.example.shangting.entity.User;
import com.example.shangting.repository.PropertyRepository;
import com.example.shangting.repository.UserRepository;
import com.example.shangting.security.JwtUtil;
import com.example.shangting.service.PropertyService;
import com.example.shangting.service.RentPredictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://localhost:5173")

public class PropertyController {
    private final RentPredictionService rentPredictionService;
    private final PropertyService propertyService;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public PropertyController(PropertyService propertyService, PropertyRepository propertyRepository, UserRepository userRepository, JwtUtil jwtUtil,RentPredictionService rentPredictionService) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.rentPredictionService = rentPredictionService;
    }


    @GetMapping("/all")
    public List<Property> getAllPropertiesFromDB() {
        return propertyService.getAllProperties(); // 调用 service 从数据库查
    }

    // ✅ 分页 + 搜索（关键字可选）
    @GetMapping("/search")
    public ResponseEntity<?> searchProperties(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String houseType,
            @RequestParam(required = false) Integer minRent,
            @RequestParam(required = false) Integer maxRent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 调用 Service 的多条件搜索方法
        Page<Property> result = propertyService.searchProperties(keyword, city, houseType, minRent, maxRent, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalPages", result.getTotalPages());
        response.put("totalElements", result.getTotalElements());

        return ResponseEntity.ok(response);
    }


    // ✅ 根据房东ID获取房源
    @GetMapping("/owner")
    public ResponseEntity<?> getPropertiesByOwner(Authentication authentication) {
        Long ownerId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(propertyService.getPropertiesByOwnerId(ownerId));
    }

    // ✅ 获取单个房源详情
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        if (property.isPresent()) {
            return ResponseEntity.ok(property.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "房源不存在"));
        }
    }



    @PostMapping
    public ApiResponse<Property> addProperty(
            @RequestBody Property property,
            @RequestHeader("Authorization") String authHeader) {
        System.out.println("收到的房源：" + property);
        // 🔹 从 token 里提取用户名
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        // 🔹 查出当前用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 🔹 自动设置 ownerId，不依赖前端传
        property.setOwnerId(user.getId());
        Map<String, Object> data = Map.of(

                "city", property.getCity(),
                "houseType", property.getHouseType()
        );
        long predictedRent = Math.round(rentPredictionService.predictRent(data));
        property.setRent((int)predictedRent);


        Property saved = propertyRepository.save(property);
        System.out.println("收到的房源：" + property);
        return ApiResponse.success(saved);
    }


    // ✅ 编辑房源（房东可修改自己的）
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id,
                                            @RequestBody Property updatedProperty,
                                            Authentication authentication) {
        Long ownerId = Long.parseLong(authentication.getName());
        Optional<Property> optionalProperty = propertyService.getPropertyById(id);

        if (optionalProperty.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "房源不存在"));
        }

        Property property = optionalProperty.get();
        if (!property.getOwnerId().equals(ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "无权修改别人的房源"));
        }

        // 更新属性
        property.setTitle(updatedProperty.getTitle());
        property.setAddress(updatedProperty.getAddress());
        property.setRent(updatedProperty.getRent());

        Property saved = propertyService.saveProperty(property);
        return ResponseEntity.ok(saved);
    }

    // ✅ 删除房源（房东或管理员）
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Long ownerId = user.getId();

        // 获取用户角色（假设User有role字段，例如 "ROLE_LANDLORD" 或 "ROLE_ADMIN"）
        String role = user.getRole();

        System.out.println("请求删除房源ID：" + id);
        System.out.println("当前登录用户：" + username + "（角色：" + role + "）");

        Optional<Property> optionalProperty = propertyService.getPropertyById(id);
        if (optionalProperty.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "房源不存在"));
        }

        Property property = optionalProperty.get();
        System.out.println("数据库房源ownerID：" + property.getOwnerId());

        // ✅ 允许管理员删除任意房源
        if (!property.getOwnerId().equals(ownerId) &&
                !("ROLE_ADMIN".equals(role) || "ADMIN".equals(role))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "无权删除别人的房源"));
        }


        propertyService.deleteProperty(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }


}
