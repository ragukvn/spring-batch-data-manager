package com.ragukvn.data.manager.mapper;

import com.ragukvn.data.manager.entity.Transaction;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction transaction);
}

