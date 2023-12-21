package com.example.bhazi.finance;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.finance.domain.WalletService;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.finance.domain.model.WalletTransaction;
import com.example.bhazi.finance.dto.WalletResponseDto;
import com.example.bhazi.finance.dto.WalletTransactionCreateDto;
import com.example.bhazi.finance.dto.WalletTransactionResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @PostMapping("/transaction")
    public ResponseDto save(@Valid @RequestBody WalletTransactionCreateDto transaction) {
        Wallet wallet = walletService.getById(transaction.getWalletId());
        WalletTransaction savedTransaction = walletService.saveTransaction(
            walletMapper.mapToModel(transaction, wallet),
            transaction.getUserId()
        );
        WalletTransactionResponseDto responseDto = walletMapper
                .mapFromTransactionModel(savedTransaction);
        return ResponseBuilder.createResponse(responseDto);
    }

    @PutMapping("/transaction/{id}")
    public ResponseDto update(
            @PathVariable(name = "id") int id,
            @RequestBody WalletTransaction transaction
    ) {
        WalletTransaction updatedTransaction = walletService.updateTransaction(id, transaction);
        return ResponseBuilder.createResponse(updatedTransaction);
    }

    @GetMapping("")
    public ResponseDto getAll() {
        List<Wallet> wallets = walletService.getAll();
        List<WalletResponseDto> responseDtos = walletMapper.mapFromModel(wallets);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") int id) {
        Wallet wallet = walletService.getById(id);
        return ResponseBuilder.createResponse(walletMapper.mapToDto(wallet));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseDto getByProfileId(@PathVariable(name = "profileId") int profileId) {
        Wallet wallet = walletService.getByProfileId(profileId);
        return ResponseBuilder.createResponse(
            walletMapper.mapToDto(wallet)
        );
    }

    @GetMapping("/transaction/{id}")
    public ResponseDto getTransactionById(@PathVariable int id) {
        WalletTransaction walletTransaction = walletService.getTransactionById(id);
        return ResponseBuilder.createResponse(
            walletMapper.mapFromTransactionModel(walletTransaction)
        );
    }

    @GetMapping("/transactions/{walletId}")
    public ResponseDto getAllByWalletId(@PathVariable int walletId) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        List<WalletTransaction> transactions = walletService.getAllByWalletId(walletId, pageable).getContent();
        List<WalletTransactionResponseDto> responseDtos = walletMapper
                .mapFromTransactionModel(transactions);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }
}
