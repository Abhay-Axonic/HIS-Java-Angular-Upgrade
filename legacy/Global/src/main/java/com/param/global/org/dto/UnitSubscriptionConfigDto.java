package com.param.global.org.dto;

import javax.persistence.Column;
import java.util.Date;

public class UnitSubscriptionConfigDto {

    private Integer subscriptionConfigId;

    private Integer unitId;

    private Integer organizationId;

    private Double subscriptionAmount;

    private Double subscriptionDiscount;

    private Double subscriptionNetAmount;

    private Double subscriptionTax;

    private Character status;

        private String subscriptionType;

    private Integer createdBy;

    private Integer updatedBy;

    private String createdDate;

    private String updatedDate;

    private String postCode;
    private String unitEmailId;
    private String unitContact;
    private String unitCode;
    private String unitName;
    private String unitAddress;

    public Integer getSubscriptionConfigId() {
        return subscriptionConfigId;
    }

    public void setSubscriptionConfigId(Integer subscriptionConfigId) {
        this.subscriptionConfigId = subscriptionConfigId;
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

    public Double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Double getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(Double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public Double getSubscriptionNetAmount() {
        return subscriptionNetAmount;
    }

    public void setSubscriptionNetAmount(Double subscriptionNetAmount) {
        this.subscriptionNetAmount = subscriptionNetAmount;
    }

    public Double getSubscriptionTax() {
        return subscriptionTax;
    }

    public void setSubscriptionTax(Double subscriptionTax) {
        this.subscriptionTax = subscriptionTax;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getUnitEmailId() {
        return unitEmailId;
    }

    public void setUnitEmailId(String unitEmailId) {
        this.unitEmailId = unitEmailId;
    }

    public String getUnitContact() {
        return unitContact;
    }

    public void setUnitContact(String unitContact) {
        this.unitContact = unitContact;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }
}
