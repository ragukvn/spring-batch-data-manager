package com.ragukvn.data.manager.service;

import com.ragukvn.data.manager.entity.Transaction;
import com.ragukvn.data.manager.mapper.TransactionMapper;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import com.ragukvn.data.manager.repository.TransactionRepository;
import com.ragukvn.data.manager.specification.TransactionSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public TransactionDto updateTransaction(TransactionDto transactionDto) {

        Transaction existing = transactionRepository.findById(transactionDto.getTransactionId())
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionDto.getTransactionId()));

        Optional.ofNullable(transactionDto.getVersion())
                .map(currentVersion -> existing.getVersion().equals(currentVersion))
                .filter(Boolean.TRUE::equals)
                .orElseThrow(() -> new OptimisticLockException("Transaction version mismatch for ID: " + transactionDto.getTransactionId()));

        transactionMapper.updateValidField(transactionDto, existing);
        Transaction updated = transactionRepository.saveAndFlush(existing);
        return transactionMapper.toDto(updated);
    }
}
