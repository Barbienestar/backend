package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Generic paginated response wrapper")
public record PagedResult<T>(
        @Schema(description = "Items on the current page") List<T> items,
        @Schema(description = "Current page number (0-based)") int page,
        @JsonProperty("page_size") @Schema(description = "Number of items per page") int pageSize,
        @JsonProperty("total_items") @Schema(description = "Total number of items across all pages") long totalItems,
        @JsonProperty("total_pages") @Schema(description = "Total number of pages") int totalPages) {
}
