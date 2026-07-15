package com.param.global.doctor.dto;

import javax.persistence.Column;
import java.util.Date;

public class OutPatientFeedbackListDTO {

    private Integer key;

    private String value;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
