package com.ragukvn.data.manager.specification;

import com.ragukvn.data.manager.entity.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ragukvn.data.manager.constant.AppConstant.ACCOUNT_NUMBER;
import static com.ragukvn.data.manager.constant.AppConstant.CUSTOMER_ID;
import static com.ragukvn.data.manager.constant.AppConstant.DESCRIPTION;
import static com.ragukvn.data.manager.constant.AppConstant.LIKE_QUERY_FORMAT;
import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class TransactionSpecification {

    public static Specification<Transaction> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(customerId)
                        .map(value -> criteriaBuilder.equal(root.get(CUSTOMER_ID), value))
                        .orElseGet(criteriaBuilder::or);
    }

    public static Specification<Transaction> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(description)
                        .filter(value -> !value.isEmpty())
                        .map(value -> criteriaBuilder.like(root.get(DESCRIPTION), String.format(LIKE_QUERY_FORMAT, value)))
                        .orElseGet(criteriaBuilder::or);
    }

    public static Specification<Transaction> hasAccountNumbers(List<String> accountNumbers) {
        return (root, query, criteriaBuilder) -> {
            if (isNull(accountNumbers) || accountNumbers.isEmpty()) {
                return criteriaBuilder.or();
            }
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get(ACCOUNT_NUMBER));
            accountNumbers.forEach(inClause::value);
            return inClause;
        };
    }

    public static Specification<Transaction> buildSearchSpec(Long customerId, List<String> accountNumbers, String description) {

        List<Specification<Transaction>> specs = new ArrayList<>();

        Optional.ofNullable(customerId).ifPresent(id -> {
            log.info("Filtering transactions by customerId");
            specs.add(hasCustomerId(id));
        });

        Optional.ofNullable(accountNumbers)
                .filter(accountNumber -> !accountNumber.isEmpty())
                .ifPresent(numbers -> {
                    log.info("Filtering transactions by account numbers");
                    specs.add(hasAccountNumbers(numbers));
                });
        Optional.ofNullable(description).filter(theDescription -> !theDescription.isEmpty())
                .ifPresent(desc -> {
                    log.info("Filtering transactions by description");
                    specs.add(hasDescription(desc));
                });

        return specs.stream()
                .reduce(Specification::or)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}
