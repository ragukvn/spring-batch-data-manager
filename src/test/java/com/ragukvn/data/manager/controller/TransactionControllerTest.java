package com.ragukvn.data.manager.controller;

import com.ragukvn.data.manager.constant.ApiPath;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import com.ragukvn.data.manager.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.ragukvn.data.manager.constant.ApiPath.BASE_TRANSACTION_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    private TransactionDto transactionDto;

    static Stream<Object[]> invalidUpdateData() {
        return Stream.of(
                new Object[]{"{\"transactionId\":1,\"description\":\"\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}", "description: Description cannot be blank or null"},
                new Object[]{"{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":-100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}", "trxAmount: Amount must be greater than or equal to zero"},
                new Object[]{"{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}", "accountNumber: Account number cannot be blank or null"},
                new Object[]{"{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":null,\"version\":0}", "customerId: Customer ID cannot be null"},
                new Object[]{"{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":-1}", "version: Version must be greater than or equal to zero"},
                new Object[]{"{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1}", "version: Version cannot be null"},
                new Object[]{"{\"transactionId\":null,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}", "transactionId: Transaction ID cannot be null"},
                new Object[]{"{\"transactionId\":0,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}", "transactionId: Transaction ID must be greater than or equal to 1"}
        );
    }

    @BeforeEach
    void setUp() {

        transactionDto = TransactionDto.builder()
                .transactionId(1L)
                .description("Test transaction")
                .build();

        Page<TransactionDto> transactionPage = new PageImpl<>(List.of(transactionDto));

        // Mock getTransactions
        when(transactionService.getTransactions(any(Pageable.class)))
                .thenReturn(transactionPage);

        // Mock search
        when(transactionService.search(any(), any(), any(), any(Pageable.class)))
                .thenReturn(transactionPage);

        // Mock update
        when(transactionService.updateTransaction(any(TransactionDto.class)))
                .thenReturn(transactionDto);
    }

    @Test
    void getTransactionsReturnWithPagination200() throws Exception {

        mockMvc.perform(get(BASE_TRANSACTION_PATH + ApiPath.ALL + "?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Test transaction"));

    }

    @Test
    void getTransactionsReturnWithoutPagination200() throws Exception {

        mockMvc.perform(get(BASE_TRANSACTION_PATH + ApiPath.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Test transaction"));

    }

    @Test
    void getTransactionsReturn400InvalidPagination() throws Exception {

        mockMvc.perform(get(BASE_TRANSACTION_PATH + ApiPath.ALL + "?page=-1&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("page: Page index must not be less than zero"))
                .andExpect(jsonPath("$.title").value("Validation Failed"));

    }

    @Test
    void searchTransaction() throws Exception {

        mockMvc.perform(get(BASE_TRANSACTION_PATH + TransactionController.SEARCH)
                        .param("customerId", "1")
                        .param("description", "Test transaction")
                        .param("accountNumbers", "1234567890")
                        .param("accountNumbers", "1234567890")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Test transaction"));
    }

    @Test
    void updateTransaction() throws Exception {
        mockMvc.perform(put(BASE_TRANSACTION_PATH)
                        .contentType("application/json")
                        .content("{\"transactionId\":1,\"description\":\"Test transaction\",\"trxAmount\":100.00,\"accountNumber\":\"1234567890\",\"trxDate\":\"2023-10-01\",\"trxTime\":\"12:00:00\",\"customerId\":1,\"version\":0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.description").value("Test transaction"));

    }

    @ParameterizedTest
    @MethodSource("invalidUpdateData")
    void updateTransactionWithInvalidData(String body, String expectedValue) throws Exception {
        mockMvc.perform(put(BASE_TRANSACTION_PATH)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value(expectedValue))
                .andExpect(jsonPath("$.title").value("Validation Failed"));

    }
}