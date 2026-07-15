package com.param.global.billing.dto;

import javax.persistence.Column;

public class FranchiseDepositMasterDto {

    private int frDepositId;

    private Integer franchiseUnitId;

    private String frDepositNo;

    private Double amount;

    private Double availableDeposit;

    private Double consumedDeposit;

    private Integer createdBy;

    private Integer updatedBy;

    private Integer orgId;

    private Integer unitId;

    private String createdDate;

    private String updatedDate;

    private String receivedFrom;

    private String identificationNo;

    private String remarks;

    private Integer counterId;

    private Integer depositTypeId;

    public Integer getDepositTypeId() {
        return depositTypeId;
    }

    public void setDepositTypeId(Integer depositTypeId) {
        this.depositTypeId = depositTypeId;
    }

    private String signetureImage;

    private byte[] signature;

    public int getFrDepositId() {
        return frDepositId;
    }

    public void setFrDepositId(int frDepositId) {
        this.frDepositId = frDepositId;
    }

    public Integer getFranchiseUnitId() {
        return franchiseUnitId;
    }

    public void setFranchiseUnitId(Integer franchiseUnitId) {
        this.franchiseUnitId = franchiseUnitId;
    }

    public String getFrDepositNo() {
        return frDepositNo;
    }

    public void setFrDepositNo(String frDepositNo) {
        this.frDepositNo = frDepositNo;
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

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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








    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public String getSignetureImage() {
        return signetureImage;
    }

    public void setSignetureImage(String signetureImage) {
        this.signetureImage = signetureImage;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAvailableDeposit() {
        return availableDeposit;
    }

    public void setAvailableDeposit(Double availableDeposit) {
        this.availableDeposit = availableDeposit;
    }

    public Double getConsumedDeposit() {
        return consumedDeposit;
    }

    public void setConsumedDeposit(Double consumedDeposit) {
        this.consumedDeposit = consumedDeposit;
    }
}
