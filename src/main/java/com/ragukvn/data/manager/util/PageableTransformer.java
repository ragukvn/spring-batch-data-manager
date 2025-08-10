package com.ragukvn.data.manager.util;

import com.ragukvn.data.manager.model.dto.PageableRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class PageableTransformer {

    public static Pageable getPageable(PageableRequest pageableRequest) {
        log.debug("Payable request {} ", pageableRequest);

        List<Sort.Order> pageable = Optional.ofNullable(pageableRequest.getSort())
                .map(theSort -> theSort.stream()
                        .filter(fieldAndOrder -> !fieldAndOrder.isEmpty())
                        .map(PageableTransformer::getOrder)
                        .toList()
                )
                .orElse(List.of(new Sort.Order(Sort.Direction.ASC, "transactionId")));

        Sort sort = pageable.isEmpty() ? Sort.unsorted() : Sort.by(pageable);
        log.debug("Creating pageable with page: {}, size: {}, sort: {}", pageableRequest.getPage(), pageableRequest.getSize(), sort);
        return PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), sort);
    }

    private static Sort.Order getOrder(String fieldAndOrder) {
        String[] parts = fieldAndOrder.split(",");
        Sort.Direction direction = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC;
        String field = parts[0];
        return new Sort.Order(direction, field);
    }
}
