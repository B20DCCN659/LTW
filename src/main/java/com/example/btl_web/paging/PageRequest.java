package com.example.btl_web.paging;

import com.example.btl_web.constant.Constant.*;

import java.util.Map;

public class PageRequest implements Pageable{
    private Integer totalPages;
    private Integer page;
    private Integer offset;
    private Integer limit;
    private String sortName;
    private String sortBy;
    public PageRequest(Map<String, String[]> paramters, Long totalItems)
    {
        String pageStr[] = paramters.get(Paging.PAGE_STR);
        if(pageStr == null)
            page = 1;
        else
            page = Integer.parseInt(pageStr[0]);

        String maxItemsPageStr[] = paramters.get(Paging.MAX_ITEMS_OF_PAGE_STR);
        if(maxItemsPageStr == null)
            limit = Paging.MAX_ITEMS_OF_PAGE;
        else
            limit = Integer.parseInt(maxItemsPageStr[0]);

        String sortNameStr[] = paramters.get(Paging.SORT_NAME);
        String sortByStr[] = paramters.get(Paging.SORT_BY);

        if(sortNameStr != null)
            sortName = sortNameStr[0];
        else
            sortName = "created_at";
        if(sortByStr != null)
        {
            sortBy = sortByStr[0];
            if(!sortBy.equals("asc") || !sortBy.equals("desc"))
                sortBy = "desc";
        }
        else
            sortBy = "desc";

        offset = 0;
        if(this.page != null && this.limit != null)
            offset = (this.page - 1) * this.limit;

        this.totalPages = (int) Math.ceil(1.0 * totalItems / limit);
    }

    @Override
    public Integer getPage() {
        return page;
    }

    @Override
    public Integer getOffset() {
        return offset;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public Integer getTotalPages() {
        return totalPages;
    }

    @Override
    public String getSortName() {
        return sortName;
    }

    @Override
    public String getSortBy() {
        return sortBy;
    }

    @Override
    public  StringBuilder addPagingation() {
        StringBuilder sb = new StringBuilder();
        if(this == null)
            return sb;
        //Thêm các tham số
        if(sortBy != null && sortName != null)
            sb.append(" ORDER BY " + sortName + " " + sortBy);
        if(offset != null && limit != null)
            sb.append(" LIMIT " + offset + "," + limit);
        return sb;
    }

    @Override
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
