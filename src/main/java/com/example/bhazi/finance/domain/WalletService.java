package com.example.bhazi.finance.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.core.exception.GlobalException;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.finance.domain.model.WalletTransaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public Wallet createWallet() {
        Wallet wallet;
        wallet = new Wallet();
        wallet.setBalance(0.0f);
        return wallet;
    }

    @Transactional
    public WalletTransaction saveTransaction(
        WalletTransaction walletTransaction, String userId
    ) {
        WalletTransaction savedWalletTransaction;
        if (!walletTransaction.getWallet().getProfile().getUserId().equals(userId)) {
            log.info("Unathorized access to update wallet balance");
            throw new GlobalException("Not authorized to update wallet balance");
        }
        savedWalletTransaction = walletTransactionRepository.save(walletTransaction);
        if (walletTransaction.isStatus())
            updateBalance(savedWalletTransaction);
        return savedWalletTransaction;
    }

    @Transactional
    public WalletTransaction updateTransaction(int id, WalletTransaction walletTransaction) {
        WalletTransaction oldWalletTransaction, updatedWalletTransaction;
        oldWalletTransaction = getTransactionById(id);
        if (oldWalletTransaction.isStatus()) {
            log.info(
                "Try to update already successful transaction {}",
                oldWalletTransaction.getId()
            );
            throw new GlobalException(
                "Not allowed to update already successful transaction"
            );
        }
        updatedWalletTransaction =  walletTransactionRepository.save(walletTransaction);
        updateBalance(updatedWalletTransaction);
        return updatedWalletTransaction;
    }

    private void updateBalance(WalletTransaction walletTransaction) {
        Wallet wallet = walletTransaction.getWallet();
        if (wallet != null) {
            float balance = wallet.getBalance();
            balance += walletTransaction.getAmount();
            wallet.setBalance(balance);
            walletRepository.save(wallet);
        } else {
            throw new EntityNotFoundException("Wallet not found");
        }
    }

    public List<Wallet> getAll() {
        return walletRepository.findAll();
    }

    public Wallet getById(int id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Wallet not found with id = " + id)
        );
        return wallet;
    }

    public Wallet getByProfileId(int profileId) {
        Wallet wallet = walletRepository.findByProfileId(profileId).orElseThrow(
            () -> new EntityNotFoundException("Wallet not found for profile with id = " + profileId)
        );
        return wallet;
    }

    public WalletTransaction getTransactionById(long id) {
        WalletTransaction walletTransaction = walletTransactionRepository
                .findById(id).orElseThrow(() -> 
                    new EntityNotFoundException(
                        "Wallet transaction not found with id = " + id
                    )
                );
        return walletTransaction;
    }

    public Page<WalletTransaction> getAllByWalletId(int walletId, Pageable pageable) {
        /* Wallet walletEntity = getById(walletId);
        return walletEntity.getTransactions(pageable); */
        return walletTransactionRepository.getAllByWalletId(walletId, pageable);
    }
}
