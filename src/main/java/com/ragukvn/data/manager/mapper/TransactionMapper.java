package com.ragukvn.data.manager.mapper;

import com.ragukvn.data.manager.entity.Transaction;
import com.ragukvn.data.manager.model.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction transaction);

    @Mapping(target = "createdOn", ignore = true)  // don't touch createdOn
    @Mapping(target = "updatedOn", ignore = true)  // updatedOn handled by JPA
    @Mapping(target = "version", ignore = true) // optimistic locking field
    Transaction updateValidField(TransactionDto transactionDto, @MappingTarget Transaction transaction);
}

