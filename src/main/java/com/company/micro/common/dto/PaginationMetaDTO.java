

package com.company.micro.common.dto;

import java.io.Serializable;

/**
 * <h1>Pagination Meta DTO</h1> It represents pagination related information in
 * response body.
 */
public class PaginationMetaDTO implements Serializable {

    private static final long serialVersionUID = 4911076807352391702L;
    private Long totalCount;
    private Integer totalPages;
    private Boolean isFirst;
    private Boolean isLast;
    private Integer pageNumber;
    private Integer pageSize;

    public PaginationMetaDTO() {
        totalCount = 0L;
        totalPages = 0;
        isFirst = Boolean.TRUE;
        isLast = Boolean.TRUE;
        pageNumber = 0;
        pageSize = 0;
    }

    public PaginationMetaDTO(Long totalCount, Integer totalPages, Boolean isFirst, Boolean isLast,
                             Integer pageNumber, Integer pageSize) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
