package com.clone.team4.domain.post.dto;

import lombok.Setter;

@Setter
public class PageParam {
    private String category = "";
    private Integer page = 1;
    private Integer size = 8;

    public String getCategory() {
        return category;
    }

    public Integer getPage() {
        if (page == 0) {
            return page;
        }
        return this.page - 1;
    }

    public Integer getSize() {
        return size;
    }
}
