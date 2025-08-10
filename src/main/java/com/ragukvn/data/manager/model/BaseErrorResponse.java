package com.ragukvn.data.manager.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BaseErrorResponse {
    private String title;
    private List<String> errors;
}
