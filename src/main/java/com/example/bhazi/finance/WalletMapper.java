package com.example.bhazi.finance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.finance.domain.model.WalletTransaction;
import com.example.bhazi.finance.dto.WalletResponseDto;
import com.example.bhazi.finance.dto.WalletTransactionCreateDto;
import com.example.bhazi.finance.dto.WalletTransactionResponseDto;
import com.example.bhazi.util.DateUtil;

// @Mapper(componentModel = "spring")
@Component
public class WalletMapper {
    
    public WalletTransaction mapToModel(
        WalletTransactionCreateDto transactionCreateDto,
        Wallet wallet
    ) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setAmount(transactionCreateDto.getAmount());
        walletTransaction.setDescription(transactionCreateDto.getDescription());
        walletTransaction.setStatus(transactionCreateDto.isStatus());
        // walletTransaction.setCreatedTimestamp(DateUtil.getTimestamp());
        walletTransaction.setWallet(wallet);
        return walletTransaction;
    }
    
    public WalletResponseDto mapToDto(Wallet wallet) {
        return WalletResponseDto.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .profileId(wallet.getProfile().getId())
                .build();
    }

    public List<WalletResponseDto> mapFromModel(List<Wallet> wallets) {
        return wallets.stream()
                .map(wallet -> mapToDto(wallet))
                .collect(Collectors.toList());
    }

    public WalletTransactionResponseDto mapFromTransactionModel(WalletTransaction transaction) {
        return WalletTransactionResponseDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .status(transaction.isStatus())
                .description(transaction.getDescription())
                .timestamp(DateUtil.getTimestampString(transaction.getCreatedAt()))
                .build();
    }

    public List<WalletTransactionResponseDto> mapFromTransactionModel(
        List<WalletTransaction> WalletTransactions
    ) {
        return WalletTransactions.stream()
                .map(walletTransaction -> mapFromTransactionModel(walletTransaction))
                .collect(Collectors.toList());
    }
}
