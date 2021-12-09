package com.nals.work.model.work;

import lombok.Getter;
import lombok.Setter;

/**
 * The model of work for request create/update
 *
 * @author HanhLe
 */
@Getter
@Setter
public class WorkRequestModel {

    /**
     * The id of work
     */
    private Long id;

    /**
     * Name of work
     */
    private String workName;

    /**
     * Start date
     */
    private String startDate;

    /**
     * End date
     */
    private String endDate;

    /**
     * Status of work
     */
    private Integer status;
}
