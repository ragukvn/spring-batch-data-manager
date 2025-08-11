package com.ragukvn.data.manager.service;

import com.ragukvn.data.manager.entity.Transaction;
import com.ragukvn.data.manager.mapper.TransactionMapper;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import com.ragukvn.data.manager.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        transactionService = new TransactionService(transactionRepository, transactionMapper);
    }

    @Test
    void getTransactions_shouldReturnMappedDtos() {
        Transaction entity = new Transaction();
        TransactionDto dto = new TransactionDto();

        when(transactionRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));
        when(transactionMapper.toDto(entity)).thenReturn(dto);

        Page<TransactionDto> result = transactionService.getTransactions(PageRequest.of(0, 10));

        assertThat(result.getContent()).containsExactly(dto);
        verify(transactionRepository).findAll(any(PageRequest.class));
        verify(transactionMapper).toDto(entity);
    }

    @Test
    void search_shouldCallSpecificationAndReturnDtos() {
        Transaction entity = new Transaction();
        TransactionDto dto = new TransactionDto();

        when(transactionRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));
        when(transactionMapper.toDto(entity)).thenReturn(dto);

        Page<TransactionDto> result = transactionService.search(1L, "desc", List.of("acc1"), PageRequest.of(0, 10));

        assertThat(result.getContent()).containsExactly(dto);
        verify(transactionRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void updateTransaction_shouldUpdateAndReturnDto_whenVersionMatches() {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(100L);
        dto.setVersion(1L);

        Transaction existing = new Transaction();
        existing.setVersion(1L);

        when(transactionRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(transactionRepository.saveAndFlush(existing)).thenReturn(existing);
        when(transactionMapper.toDto(existing)).thenReturn(dto);

        TransactionDto result = transactionService.updateTransaction(dto);

        assertThat(result).isSameAs(dto);
        verify(transactionMapper).updateValidField(dto, existing);
        verify(transactionRepository).saveAndFlush(existing);
    }

    @Test
    void updateTransaction_shouldThrowEntityNotFoundException_whenIdNotFound() {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(200L);

        when(transactionRepository.findById(200L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.updateTransaction(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Transaction not found");
    }

    @Test
    void updateTransaction_shouldThrowOptimisticLockException_whenVersionMismatch() {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(300L);
        dto.setVersion(1L);

        Transaction existing = new Transaction();
        existing.setVersion(2L); // mismatch

        when(transactionRepository.findById(300L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> transactionService.updateTransaction(dto))
                .isInstanceOf(OptimisticLockException.class)
                .hasMessageContaining("version mismatch");
    }
}
