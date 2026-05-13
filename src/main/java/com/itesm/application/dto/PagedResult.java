package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PagedResult<T>(
        List<T> items,
        int page,
        @JsonProperty("page_size") int pageSize,
        @JsonProperty("total_items") long totalItems,
        @JsonProperty("total_pages") int totalPages) {}
