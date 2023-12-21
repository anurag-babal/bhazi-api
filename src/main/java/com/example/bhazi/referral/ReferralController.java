package com.example.bhazi.referral;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.referral.domain.ReferralService;
import com.example.bhazi.referral.domain.model.Referral;
import com.example.bhazi.referral.domain.model.ReferralTransaction;
import com.example.bhazi.referral.dto.ReferralTransactionCreateDto;
import com.example.bhazi.referral.dto.ReferralTransactionResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/referral")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService referralService;
    private final ReferralMapper referralMapper;
    public static final int REFERRAL_PAGE_SIZE = 20;

    @PostMapping("/wallet/transaction")
    public ResponseDto saveTransaction(
        @Valid @RequestBody ReferralTransactionCreateDto referralTransactionCreateDto
    ) {
        Referral referral = referralService.getById(
            referralTransactionCreateDto.getReferralId()
        );
        ReferralTransaction savedTransaction = referralService.saveTransaction(
            referralMapper.mapToTransactionDomain(referralTransactionCreateDto,referral)
        );
        return ResponseBuilder.createResponse(
            referralMapper.mapToTransactionDto(savedTransaction)
        );
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") int id) {
        Referral referral = referralService.getById(id);
        // List<Referral> children = referralService.getAllChildren(referral.getProfile().getId());
        List<Referral> children = referralService.getAllChildren(referral.getId());
        return ResponseBuilder.createResponse(
            referralMapper.mapToDto(referral, children)
        );
    }

    @GetMapping("/transaction/{id}")
    public ResponseDto getTransactionById(@PathVariable(name = "id") long id) {
        ReferralTransaction referralTransaction = referralService.getTransactionById(id);
        return ResponseBuilder.createResponse(
            referralMapper.mapToTransactionDto(referralTransaction)
        );
    }

    @GetMapping("/transactions/{referralId}")
    public ResponseDto getTransactionsByReferralId(
        @PathVariable(name = "referralId") int referralId,
        @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(
            page, REFERRAL_PAGE_SIZE, Sort.by("id").descending()
        );
        Page<ReferralTransaction> referralTransactions = referralService
                .getTransactionsByReferralId(referralId, pageable);

        List<ReferralTransactionResponseDto> responseDtos = referralMapper
                .mapToTransactionDtos(referralTransactions.getContent());

        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    // Deprecated
    @GetMapping("/profile/{profileId}")
    public ResponseDto getByProfileId(@PathVariable(name = "profileId") int profileId) {
        Referral referral = referralService.getByProfileId(profileId);
        List<Referral> children = referralService.getAllChildren(referral.getId());
        return ResponseBuilder.createResponse(
            referralMapper.mapToDto(referral, children)
        );
    }

    @GetMapping("/wallet/{id}")
    public ResponseDto getWalletById(@PathVariable(name = "id") int id) {
        Referral referralWallet = referralService.getById(id);
        return ResponseBuilder.createResponse(
            referralMapper.mapToDto(referralWallet)
        );
    }

    /* @GetMapping("/wallet/referral/{referralId}")
    public ResponseDto getWalletByReferralId(@PathVariable(name = "referralId") int referralId) {
        ReferralWalletResponseDto referralWallet = referralService.getWalletByReferralId(referralId);
        return ResponseBuilder.createResponse(referralWallet);
    } */

    /* @GetMapping("/wallet/transaction/wallet/{walletId}")
    public ResponseDto getTransactionsByWalletId(@PathVariable(name = "walletId") int walletId) {
        Pageable pageable = PageRequest.of(0, 10);
        List<ReferralTransaction> referralTransactions = referralService
                .getTransactionsByWalletId(walletId, pageable);
        return ResponseBuilder.createListResponse(referralTransactions.size(), referralTransactions);
    } */
}
