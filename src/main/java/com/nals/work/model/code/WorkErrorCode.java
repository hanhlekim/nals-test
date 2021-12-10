package com.nals.work.model.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HanhLe
 */
@Getter
@AllArgsConstructor
public enum WorkErrorCode {

    ERROR_CODE_2001(2001, "Work id must be not null."),
    ERROR_CODE_2002(2002, "Work id not exist."),
    ERROR_CODE_2003(2003, "Format date wrong. Expected dd/MM/yyyy"),
    ERROR_CODE_2004(2004, "End date must be equal or after start date."),
    ERROR_CODE_2005(2005, "Work name is required."),
    ERROR_CODE_2006(2006, "Status not exist."),
    ERROR_CODE_2007(2007, "PageSize and PageNumber incorrect."),
    ERROR_CODE_2008(2008, "Order info incorrect.");

    private Integer code;
    private String description;

}
