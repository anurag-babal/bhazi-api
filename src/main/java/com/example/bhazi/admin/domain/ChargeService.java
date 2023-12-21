package com.example.bhazi.admin.domain;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.bhazi.admin.domain.model.Charge;
import com.example.bhazi.core.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;

    public Charge save(Charge charge) {
        return chargeRepository.save(charge);
    }

    public boolean delete(int id) {
        Charge charge = getById(id);
        chargeRepository.delete(charge);
        return true;
    }

    public Charge getById(int id) {
        Charge charge = chargeRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Charge not present with id = " + id)
        );
        return charge;
    }

    public Charge getLatest() {
        Charge charge = chargeRepository.findLatestEntity().orElseThrow(
            () -> new EntityNotFoundException("No charges available")
        );
        return charge;
    }

    public List<Charge> getAll() {
        return chargeRepository.findAll();
    }
}
