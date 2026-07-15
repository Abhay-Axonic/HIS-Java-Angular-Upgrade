package com.param.adt.admission.dto;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class HandHygieneScoreDTO {

    private Integer handHygieneScoreId;

    private Integer wardId;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private List<HandHygieneScoreListDTO> listOfSelectedCheckbox;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getHandHygieneScoreId() {
        return handHygieneScoreId;
    }

    public void setHandHygieneScoreId(Integer handHygieneScoreId) {
        this.handHygieneScoreId = handHygieneScoreId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public List<HandHygieneScoreListDTO> getListOfSelectedCheckbox() {
        return listOfSelectedCheckbox;
    }

    public void setListOfSelectedCheckbox(List<HandHygieneScoreListDTO> listOfSelectedCheckbox) {
        this.listOfSelectedCheckbox = listOfSelectedCheckbox;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
