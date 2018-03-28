package com.hulk.delivery.entity;

import java.io.Serializable;

/**
 * Created by hulk-out on 2017/11/28.
 */

public class ResponseDataList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String endRow;
    private String firstPage;
    private String hasNextPage;
    private String hasPreviousPage;
    private String isFirstPage;
    private String isLastPage;
    private String lastPage;
    private String navigateFirstPage;
    private String navigateLastPage;
    private String navigatePages;
//    private String navigatepageNums;
    private String nextPage;
    private String orderBy;
    private String pageNum;
    private String pageSize;
    private String pages;
    private String prePage;
    private String size;
    private String startRow;
    private String total;

    public String getEndRow() {
        return endRow;
    }

    public void setEndRow(String endRow) {
        this.endRow = endRow;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(String hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(String hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public String getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(String isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public String getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(String isLastPage) {
        this.isLastPage = isLastPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(String navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public String getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(String navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public String getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(String navigatePages) {
        this.navigatePages = navigatePages;
    }

//    public String getNavigatepageNums() {
//        return navigatepageNums;
//    }
//
//    public void setNavigatepageNums(String navigatepageNums) {
//        this.navigatepageNums = navigatepageNums;
//    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPrePage() {
        return prePage;
    }

    public void setPrePage(String prePage) {
        this.prePage = prePage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartRow() {
        return startRow;
    }

    public void setStartRow(String startRow) {
        this.startRow = startRow;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
