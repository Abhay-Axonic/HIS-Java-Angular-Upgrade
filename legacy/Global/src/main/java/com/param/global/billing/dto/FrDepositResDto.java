package com.param.global.billing.dto;

import java.math.BigDecimal;
import java.util.Date;

public class FrDepositResDto {

    private String frDepositNo;
    private String frDepositDate;
    private Date frDepositDateNew;
    private BigDecimal amount;
    private BigDecimal consumedDeposit;
    private BigDecimal availableDeposit;
    private Integer frDepositId;
    private String createdBy;
    private String unitCode;
    private String unitName;

    public String getFrDepositNo() {
        return frDepositNo;
    }

    public void setFrDepositNo(String frDepositNo) {
        this.frDepositNo = frDepositNo;
    }

    public String getFrDepositDate() {
        return frDepositDate;
    }

    public void setFrDepositDate(String frDepositDate) {
        this.frDepositDate = frDepositDate;
    }

    public Date getFrDepositDateNew() {
        return frDepositDateNew;
    }

    public void setFrDepositDateNew(Date frDepositDateNew) {
        this.frDepositDateNew = frDepositDateNew;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConsumedDeposit() {
        return consumedDeposit;
    }

    public void setConsumedDeposit(BigDecimal consumedDeposit) {
        this.consumedDeposit = consumedDeposit;
    }

    public BigDecimal getAvailableDeposit() {
        return availableDeposit;
    }

    public void setAvailableDeposit(BigDecimal availableDeposit) {
        this.availableDeposit = availableDeposit;
    }

    public Integer getFrDepositId() {
        return frDepositId;
    }

    public void setFrDepositId(Integer frDepositId) {
        this.frDepositId = frDepositId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
}
