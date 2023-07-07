package com.example.btl_web.paging;

public interface Pageable {
    Integer getPage();
    Integer getOffset();
    Integer getLimit();
    Integer getTotalPages();
    String getSortName();
    String getSortBy();
    StringBuilder addPagingation();
    void setSortBy(String sortBy);
    void setSortName(String sortName);
    void setLimit(Integer limit);
}
