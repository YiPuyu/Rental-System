package com.example.shangting.repository;

import com.example.shangting.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    // 按标题或地址模糊搜索

    List<Property> findByOwnerId(Long ownerId);
    Page<Property> findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String title, String address, Pageable pageable);


}

