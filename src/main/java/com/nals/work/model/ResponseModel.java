package com.nals.work.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HanhLe
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponseModel {

    private Boolean success;
    private String errorMessage;
    private Object result;
}
