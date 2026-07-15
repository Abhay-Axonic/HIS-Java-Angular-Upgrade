package com.param.pharmacy.inPatient.dto.patientConsumableIssueList;

import com.param.service.global.model.CookieObjectDto;

public class PatientDetailsRequestForDrugRecall extends CookieObjectDto{
    private Integer batchId;

    private Integer itemId;

    private Integer storeId;

    private Integer statusId;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
