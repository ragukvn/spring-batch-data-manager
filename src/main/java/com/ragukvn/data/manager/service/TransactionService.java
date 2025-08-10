package com.ragukvn.data.manager.service;

import com.ragukvn.data.manager.entity.Transaction;
import com.ragukvn.data.manager.mapper.TransactionMapper;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import com.ragukvn.data.manager.repository.TransactionRepository;
import com.ragukvn.data.manager.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public Page<TransactionDto> getTransactions(Pageable pageable) {
        return transactionRepository
                .findAll(pageable)
                .map(transactionMapper::toDto);
    }

    public Page<TransactionDto> search(Long customerId, String description, List<String> accountNumbers, Pageable pageable) {

        Specification<Transaction> specification = TransactionSpecification.buildSearchSpec(customerId, accountNumbers, description);

        return transactionRepository.findAll(specification, pageable).map(transactionMapper::toDto);
    }

    public void updateTransaction(TransactionDto transactionDto) {
        // Logic to update a transaction
    }
}
