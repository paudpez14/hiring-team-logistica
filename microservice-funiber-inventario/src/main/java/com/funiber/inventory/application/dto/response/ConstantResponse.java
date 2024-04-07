package com.funiber.inventory.application.dto.response;

public enum ConstantResponse {
    RESULT_OK("OK"),
    RESULT_KO("KO"),
    /* Messages Responses */
    OK(""),
    NOT_FOUND("Sample not found"),
    PRIMARY_KEY("Sample already exists"),
    NOT_UPDATED("Sample not updated"),
    NOT_DELETED("Sample not deleted"),
    ;

    public final String desc;

    private ConstantResponse(String desc) {
        this.desc = desc;
    }
}
