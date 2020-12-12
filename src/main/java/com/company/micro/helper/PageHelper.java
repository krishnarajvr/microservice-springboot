package com.company.micro.helper;

import com.company.micro.common.dto.PaginationMetaDTO;
import org.springframework.data.domain.Page;

public class PageHelper {

    public static <T> PaginationMetaDTO createPaginationMeta(Page<T> page) {
        PaginationMetaDTO paginationMeta = new PaginationMetaDTO();

        paginationMeta.setTotalCount(page.getTotalElements());
        paginationMeta.setTotalPages(page.getTotalPages());
        paginationMeta.setPageSize(page.getSize());
        paginationMeta.setPageNumber(page.getNumber() + 1);
        paginationMeta.setIsFirst(page.isFirst());
        paginationMeta.setIsLast(page.isLast());

        return paginationMeta;
    }
}
