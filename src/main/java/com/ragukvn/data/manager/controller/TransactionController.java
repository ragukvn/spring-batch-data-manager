package com.ragukvn.data.manager.controller;


import com.ragukvn.data.manager.constant.ApiPath;
import com.ragukvn.data.manager.model.dto.PageableRequest;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import com.ragukvn.data.manager.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ragukvn.data.manager.constant.ApiPath.BASE_TRANSACTION_PATH;
import static com.ragukvn.data.manager.util.PageableTransformer.getPageable;

@Tag(name = "Transaction", description = "Transaction management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_TRANSACTION_PATH)
@Validated
public class TransactionController {

    public static final String SEARCH = "/search";
    private final TransactionService transactionService;

    @Operation(summary = "Get all transactions", description = "Retrieves a paginated list of all transactions")
    @GetMapping(ApiPath.ALL)
    public Page<TransactionDto> getTransactions(@Valid PageableRequest pageRequest) {
        // This method should return a list of transactions
        Pageable pageable = getPageable(pageRequest);
        return transactionService.getTransactions(pageable); // Placeholder for actual implementation
    }


    @Operation(summary = "Search transactions", description = "Search transactions based on customer ID, description, and account numbers")
    @GetMapping(SEARCH)
    public Page<TransactionDto> searchTransaction(
            @Valid PageableRequest pageRequest,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> accountNumbers) {
        // This method should return a list of transactions
        Pageable pageable = getPageable(pageRequest);
        return transactionService.search(customerId, description, accountNumbers, pageable); // Placeholder for actual implementation
    }


    @Operation(summary = "Update a transaction", description = "Updates an existing transaction with the provided details")
    @PutMapping
    public TransactionDto updateTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        // This method should return a list of transactions
        return transactionService.updateTransaction(transactionDto); // Placeholder for actual implementation
    }

}
