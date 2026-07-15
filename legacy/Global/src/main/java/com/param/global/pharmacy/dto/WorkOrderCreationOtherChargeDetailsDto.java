package com.param.global.pharmacy.dto;


public class WorkOrderCreationOtherChargeDetailsDto {
	
    private Integer workorderRequestId ;
    private Integer otherchargeTypeId ;
    private Double otherChargeAmount ;
    private String status ;
    private String createdDate ;
    private Integer  createdBy ;
    private String  updatedDate ;
    private Integer  updatedBy ;
    private Integer  unitId ;
    private Integer  orgId ;
    private Double otherChargeAmountOld;

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}
	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}
	public Integer getOtherchargeTypeId() {
		return otherchargeTypeId;
	}
	public void setOtherchargeTypeId(Integer otherchargeTypeId) {
		this.otherchargeTypeId = otherchargeTypeId;
	}
	public Double getOtherChargeAmount() {
		return otherChargeAmount;
	}
	public void setOtherChargeAmount(Double otherChargeAmount) {
		this.otherChargeAmount = otherChargeAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

    public Double getOtherChargeAmountOld() {
        return otherChargeAmountOld;
    }

    public void setOtherChargeAmountOld(Double otherChargeAmountOld) {
        this.otherChargeAmountOld = otherChargeAmountOld;
    }
}
