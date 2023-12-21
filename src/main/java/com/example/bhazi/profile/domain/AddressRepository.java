package com.example.bhazi.profile.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.profile.domain.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByProfileIdAndIsActive(
        Integer profileId, Boolean isActive, Pageable pageable
    );
    
    Optional<Address> findTopByProfileIdAndIsActive(int profileId, boolean isActive);
}
