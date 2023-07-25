package com.clone.team4.domain.post.dto;

import lombok.Getter;

@Getter
public class PageDto<T> {
    private boolean hasNextPage;
    private T data;

    public PageDto(boolean hasNextPage, T data) {
        this.hasNextPage = hasNextPage;
        this.data = data;
    }
}
