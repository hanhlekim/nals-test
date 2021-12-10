package com.nals.work.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResult<T> {

    private List<T> content;
    private int totalPage;
    private long totalElements;
    private int page;
}
