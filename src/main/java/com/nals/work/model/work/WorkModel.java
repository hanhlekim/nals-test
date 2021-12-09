package com.nals.work.model.work;

import com.nals.work.entity.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * Work model.
 *
 * @author HanhLe
 */
@Getter
@Setter
public class WorkModel {

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
    private Status status;
}
