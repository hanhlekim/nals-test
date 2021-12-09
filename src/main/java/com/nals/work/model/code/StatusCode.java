package com.nals.work.model.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HanhLe
 */
@Getter
@AllArgsConstructor
public enum StatusCode {

    PLANNING(1, "Planning"),
    DOING(2, "Doing"),
    COMPLETE(3, "Complete");

    private Integer code;
    private String description;

    public static StatusCode getStatusCodeByCode(Integer code) {
        for (StatusCode value : StatusCode.values()) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return null;
    }
}

