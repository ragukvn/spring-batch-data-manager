package com.ragukvn.data.manager.util;

import com.ragukvn.data.manager.model.dto.PageableRequest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageableTransformerTest {

    @Test
    void shouldUseDefaultSortWhenSortIsNull() {
        PageableRequest request = new PageableRequest();
        request.setPage(0);
        request.setSize(5);
        request.setSort(null); // triggers default transactionId ASC

        Pageable pageable = PageableTransformer.getPageable(request);

        assertThat(pageable.getSort().getOrderFor("transactionId")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("transactionId").getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void shouldIgnoreEmptySortEntries() {
        PageableRequest request = new PageableRequest();
        request.setPage(1);
        request.setSize(10);
        request.setSort(List.of("", "name,desc")); // empty string ignored

        Pageable pageable = PageableTransformer.getPageable(request);

        assertThat(pageable.getSort().getOrderFor("name")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("name").getDirection())
                .isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void shouldDefaultDirectionToAscWhenNotProvided() {
        PageableRequest request = new PageableRequest();
        request.setPage(0);
        request.setSize(20);
        request.setSort(List.of("name")); // no direction provided

        Pageable pageable = PageableTransformer.getPageable(request);

        assertThat(pageable.getSort().getOrderFor("name").getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void shouldReturnUnsortedWhenNoValidSortsProvided() {
        PageableRequest request = new PageableRequest();
        request.setPage(2);
        request.setSize(15);
        request.setSort(List.of("")); // all invalid

        Pageable pageable = PageableTransformer.getPageable(request);

        assertThat(pageable.getSort()).isEqualTo(Sort.unsorted());
    }
}
