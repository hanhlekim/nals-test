package com.nals.work.model.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HanhLe
 */
@Getter
@AllArgsConstructor
public enum WorkErrorCode {

    ERROR_CODE_2001(2001, "Work id must be not null"),
    ERROR_CODE_2002(2002, "Work id not exist");

    private Integer code;
    private String description;

    public static WorkErrorCode getWorkErrorCodeByCode(Integer code) {
        for (WorkErrorCode value : WorkErrorCode.values()) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return null;
    }
}
