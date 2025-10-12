package com.example.shangting.controller;



import com.example.shangting.entity.Property;
import com.example.shangting.repository.PropertyRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlord")
public class LandlordController {

    private final PropertyRepository propertyRepository;

    public LandlordController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // 🔹 房东发布房源
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/properties")
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    // 🔹 房东查看自己发布的房源
    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping("/properties")
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
}
