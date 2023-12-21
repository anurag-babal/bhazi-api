package com.example.bhazi.finance;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.finance.domain.TransactionService;
import com.example.bhazi.finance.domain.model.Transaction;
import com.example.bhazi.finance.dto.TransactionCreateDto;
import com.example.bhazi.finance.dto.TransactionResponseDto;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final ProfileService profileService;

    @PostMapping("")
    public ResponseDto save(@RequestBody TransactionCreateDto transactionCreateDto) {
        Profile profile = profileService.getById(transactionCreateDto.getProfileId());
        Transaction transaction = transactionService.save(
            transactionMapper.mapToDomain(transactionCreateDto, profile)
        );
        return ResponseBuilder.createResponse(
            transactionMapper.mapToDto(transaction)
        );
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable long id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseBuilder.createResponse(
            transactionMapper.mapToDto(transaction)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseDto getById(@PathVariable int profileId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        List<Transaction> transactions = transactionService.getByProfileId(profileId, pageable);
        List<TransactionResponseDto> responseDtos = transactionMapper.mapToDtos(transactions);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }
}
