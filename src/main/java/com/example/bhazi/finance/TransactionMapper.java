package com.example.bhazi.finance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.finance.domain.model.PaymentMode;
import com.example.bhazi.finance.domain.model.Transaction;
import com.example.bhazi.finance.dto.TransactionCreateDto;
import com.example.bhazi.finance.dto.TransactionResponseDto;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.DateUtil;

@Component
public class TransactionMapper {
    
    public Transaction mapToDomain(TransactionCreateDto transactionCreateDto, Profile profile) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionCreateDto.getAmount());
        transaction.setPaymentMode(
            PaymentMode.valueOf(transactionCreateDto.getPaymentMode().toUpperCase())
        );
        transaction.setStatus(transactionCreateDto.isStatus());
        transaction.setDescription(transactionCreateDto.getDescription());
        transaction.setProfile(profile);

        return transaction;
    }

    public TransactionResponseDto mapToDto(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .paymentMode(transaction.getPaymentMode().getDescription())
                .status(transaction.isStatus())
                .name(transaction.getProfile().getFirstName())
                .phoneNumber(transaction.getProfile().getPhoneNumber())
                .creationTimestamp(DateUtil.getTimestampString(transaction.getCreatedAt()))
                .build();
    }

    public List<TransactionResponseDto> mapToDtos(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> mapToDto(transaction))
                .collect(Collectors.toList());
    }
}
