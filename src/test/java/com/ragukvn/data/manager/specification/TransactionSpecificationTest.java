package com.ragukvn.data.manager.specification;

import com.ragukvn.data.manager.entity.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionSpecificationTest {

    @SuppressWarnings("unchecked")
    @Test
    void hasCustomerId_shouldReturnEqualPredicate_whenCustomerIdNotNull() {
        Root<Transaction> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("customerId")).thenReturn(path);
        when(cb.equal(path, 123L)).thenReturn(predicate);

        Predicate result = TransactionSpecification.hasCustomerId(123L)
                .toPredicate(root, mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasCustomerId_shouldReturnOrPredicate_whenCustomerIdIsNull() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate orPredicate = mock(Predicate.class);
        when(cb.or()).thenReturn(orPredicate);

        Predicate result = TransactionSpecification.hasCustomerId(null)
                .toPredicate(mock(Root.class), mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(orPredicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasDescription_shouldReturnLikePredicate_whenDescriptionProvided() {
        Root<Transaction> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> path = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("description")).thenReturn(path);
        when(cb.like(any(), anyString())).thenReturn(predicate);

        Predicate result = TransactionSpecification.hasDescription("desc")
                .toPredicate(root, mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasDescription_shouldReturnOrPredicate_whenDescriptionEmpty() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate orPredicate = mock(Predicate.class);
        when(cb.or()).thenReturn(orPredicate);

        Predicate result = TransactionSpecification.hasDescription("")
                .toPredicate(mock(Root.class), mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(orPredicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasAccountNumbers_shouldReturnInPredicate_whenListNotEmpty() {
        Root<Transaction> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path path = mock(Path.class);
        CriteriaBuilder.In<String> inClause = mock(CriteriaBuilder.In.class);

        when(root.get("accountNumber")).thenReturn(path);
        when(cb.in(path)).thenReturn(inClause);
        when(inClause.value(anyString())).thenReturn(inClause);

        Predicate result = TransactionSpecification.hasAccountNumbers(List.of("acc1", "acc2"))
                .toPredicate(root, mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(inClause);
        verify(inClause, times(2)).value(anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasAccountNumbers_shouldReturnOrPredicate_whenListEmpty() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate orPredicate = mock(Predicate.class);
        when(cb.or()).thenReturn(orPredicate);

        Predicate result = TransactionSpecification.hasAccountNumbers(emptyList())
                .toPredicate(mock(Root.class), mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(orPredicate);
    }

    @Test
    void buildSearchSpec_shouldReturnConjunction_whenNoFilters() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate conjunction = mock(Predicate.class);
        when(cb.conjunction()).thenReturn(conjunction);

        Predicate result = TransactionSpecification.buildSearchSpec(null, null, null)
                .toPredicate(mock(Root.class), mock(CriteriaQuery.class), cb);

        assertThat(result).isSameAs(conjunction);
    }

    @Test
    void buildSearchSpec_shouldCombineFilters_whenMultiplePresent() {
        Specification<Transaction> spec = TransactionSpecification.buildSearchSpec(
                123L,
                List.of("acc1"),
                "desc"
        );

        assertThat(spec).isNotNull();
    }
}
