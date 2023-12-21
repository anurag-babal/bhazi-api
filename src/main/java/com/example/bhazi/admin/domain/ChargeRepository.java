package com.example.bhazi.admin.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bhazi.admin.domain.model.Charge;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {

    @Query(value = "select * from charge order by id desc limit 1", nativeQuery = true)
    Optional<Charge> findLatestEntity(); 
}
