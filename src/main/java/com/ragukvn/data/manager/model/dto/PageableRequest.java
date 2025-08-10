package com.ragukvn.data.manager.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class PageableRequest {

    @Min(value = 0, message = "Page index must not be less than zero")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 20, message = "Page size must not be greater than 20")
    private int size = 10;

    private List<String> sort;

}
