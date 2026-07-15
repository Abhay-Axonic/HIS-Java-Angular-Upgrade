package com.param.global.dto;

public class PayeeRefundMpprDto {
    private Integer payeeId;
    private Double payeeRefundAmount;
    private Integer associateCompanyId;

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public Double getPayeeRefundAmount() {
        return payeeRefundAmount;
    }

    public void setPayeeRefundAmount(Double payeeRefundAmount) {
        this.payeeRefundAmount = payeeRefundAmount;
    }

    public Integer getAssociateCompanyId() {
        return associateCompanyId;
    }

    public void setAssociateCompanyId(Integer associateCompanyId) {
        this.associateCompanyId = associateCompanyId;
    }
}
