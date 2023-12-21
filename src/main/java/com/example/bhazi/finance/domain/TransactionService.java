package com.example.bhazi.finance.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.finance.domain.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRespository transactionRespository;

    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRespository.save(transaction);
    }

    public Transaction getById(long id) {
        Transaction transaction = transactionRespository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Transaction not found with id = " + id)
        );
        return transaction;
    }

    public List<Transaction> getByProfileId(int profileId, Pageable pageable) {
        return transactionRespository.findByProfileId(profileId, pageable).getContent();
    }
}
