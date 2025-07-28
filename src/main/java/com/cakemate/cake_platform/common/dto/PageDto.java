package com.cakemate.cake_platform.common.dto;

public class PageDto {

    private int currentPage;
    private int size;
    private int totalPages;
    private long totalElements;

    public PageDto(int currentPage, int size, int totalPages, long totalElements) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
