package com.param.global.org.dto;

import com.param.actionmaster.DateUtils.DateUtils;

import javax.persistence.Column;
import java.util.Date;
import java.util.Optional;

public class UnitSubscriptionDetailsDto {

    private Integer subscriptionId;

    private Integer subscriptionConfigId;

    private String subscriptionType;

    private Double subscriptionAmount;

    private Double subscriptionDiscount;

    private Double subscriptionTax;

    private Double subscriptionNetAmount;

    private Double paidAmount;

    private String amountPaidStatus;

    private Date subscriptionStartDate;

    private Date subscriptionEndDate;

    private String subscriptionStartDateString;

    private String subscriptionEndDateString;

    private String paymentOrderId;

    private String subscriptionStatus;

    private String paymentType;

    private Integer finalReminderCount;

    private Integer reminderCount;

    private Integer createdBy;

    private Integer updatedBy;

    private Character status;

    private String createdDate;

    private String updatedDate;

    private Integer organizationId;
    private String lastSubscriptionEndDate;
    private Integer unitId;
    private String remark;
    private String postCode;
    private String unitEmailId;
    private String unitContact;
    private String unitCode;
    private String unitName;
    private String unitAddress;
    private Boolean futureSubscriptionTaken;

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getSubscriptionConfigId() {
        return subscriptionConfigId;
    }

    public void setSubscriptionConfigId(Integer subscriptionConfigId) {
        this.subscriptionConfigId = subscriptionConfigId;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Double getSubscriptionAmount() {
        return Optional.ofNullable(subscriptionAmount).orElse(0.0);
    }

    public void setSubscriptionAmount(Double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Double getSubscriptionDiscount() {
        return Optional.ofNullable(subscriptionDiscount).orElse(0.0);
    }

    public void setSubscriptionDiscount(Double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public Double getSubscriptionNetAmount() {
        return Optional.ofNullable(subscriptionNetAmount).orElse(0.0);
    }

    public void setSubscriptionNetAmount(Double subscriptionNetAmount) {
        this.subscriptionNetAmount = subscriptionNetAmount;
    }

    public Double getPaidAmount() {
        return Optional.ofNullable(paidAmount).orElse(0.0);
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getAmountPaidStatus() {
        return amountPaidStatus;
    }

    public void setAmountPaidStatus(String amountPaidStatus) {
        this.amountPaidStatus = amountPaidStatus;
    }

    public Date getSubscriptionStartDate() {
        if(subscriptionStartDateString != null){
            return DateUtils.getDate(this.subscriptionStartDateString,"yyyy-MM-dd");
        }
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(Date subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public Date getSubscriptionEndDate() {
        if(subscriptionEndDateString != null){
            return DateUtils.getDate(this.subscriptionEndDateString,"yyyy-MM-dd");
        }
       return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(Date subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public String getSubscriptionStartDateString() {
        return subscriptionStartDateString;
    }

    public void setSubscriptionStartDateString(String subscriptionStartDateString) {
        this.subscriptionStartDateString = subscriptionStartDateString;
    }

    public String getSubscriptionEndDateString() {
        return subscriptionEndDateString;
    }

    public void setSubscriptionEndDateString(String subscriptionEndDateString) {
        this.subscriptionEndDateString = subscriptionEndDateString;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getFinalReminderCount() {
        return finalReminderCount;
    }

    public void setFinalReminderCount(Integer finalReminderCount) {
        this.finalReminderCount = finalReminderCount;
    }

    public Integer getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(Integer reminderCount) {
        this.reminderCount = reminderCount;
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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

    public Double getSubscriptionTax() {
        return Optional.ofNullable(subscriptionTax).orElse(0.0);
    }

    public void setSubscriptionTax(Double subscriptionTax) {
        this.subscriptionTax = subscriptionTax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastSubscriptionEndDate() {
        return lastSubscriptionEndDate;
    }

    public void setLastSubscriptionEndDate(String lastSubscriptionEndDate) {
        this.lastSubscriptionEndDate = lastSubscriptionEndDate;
    }

    public Boolean getFutureSubscriptionTaken() {
        return futureSubscriptionTaken;
    }

    public void setFutureSubscriptionTaken(Boolean futureSubscriptionTaken) {
        this.futureSubscriptionTaken = futureSubscriptionTaken;
    }
}
