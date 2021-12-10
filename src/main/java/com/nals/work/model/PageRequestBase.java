package com.nals.work.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HanhLe
 */
@Getter
@Setter
public class PageRequestBase {

    /**
     * The page size
     */
    private int pageSize;

    /**
     * The page number
     */
    private int pageNumber;

    /**
     * The sort column name
     */
    private String sortColumn;

    /**
     * The sort order
     */
    private String sortOrder;
}
