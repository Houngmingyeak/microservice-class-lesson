package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.domain.dto.PageResult;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
    public static <S, R> PageResponse<R> from(PageResult<S> result, Function<S, R> mapper) {
        return new PageResponse<>(
                result.content().stream().map(mapper).toList(),
                result.totalElements(),
                result.totalPages(),
                result.page(),
                result.size());
    }
}