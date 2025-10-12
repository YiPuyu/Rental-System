package com.example.shangting.service;

import com.example.shangting.entity.Property;


import com.example.shangting.entity.Property;
import com.example.shangting.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // 获取所有房源
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // 根据房东ID获取房源
    public List<Property> getPropertiesByOwnerId(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }

    // 根据房源ID获取房源
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    // 保存房源
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    // 删除房源
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    // 根据关键词分页搜索（标题或地址）
    public Page<Property> searchByKeyword(String keyword, Pageable pageable) {
        return propertyRepository.findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, keyword, pageable);
    }

    // 多条件搜索（关键词、城市、房型、租金区间）
    public Page<Property> searchProperties(
            String keyword,
            String city,
            String houseType,
            Integer minRent,
            Integer maxRent,
            Pageable pageable) {

        Specification<Property> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 关键词匹配标题或描述
            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                ));
            }

            // 城市筛选
            // 城市筛选改为模糊匹配
            if (StringUtils.hasText(city)) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }


            // 房型筛选
            if (StringUtils.hasText(houseType)) {
                predicates.add(cb.equal(root.get("houseType"), houseType));
            }

            // 租金下限
            if (minRent != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rent"), minRent));
            }

            // 租金上限
            if (maxRent != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rent"), maxRent));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return propertyRepository.findAll(spec, pageable);
    }
}
