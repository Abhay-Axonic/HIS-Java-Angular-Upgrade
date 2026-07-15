package com.param.bloodBank.dto;

import java.util.List;

public class CrossMatchRegisterDto {

	private Integer crossMatchRegisterId;
	
	private Integer patientRequisitionId;
	
	private Integer bloodComponentOrderId;
	
	private String requisitionDate;
	
	private Integer componentId;
	
	private Integer requiredBloodGroup;
	
	private Integer patientBloodGroup;
	
	private Integer requiredQty;
	
	private Integer pendingQty;
	
	private Integer btoId;
	
	private String segmentNo;
	
	private Integer technicianId;
	
	private Integer qty;
	
	private String remark;
	
	private Integer crossMatchStatus;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String issueDate;
	
	private Integer patientId;
	
	private String fromDate;
	
	private String toDate;
	
	private String patientName;
	
	private String bloodComponentDesc;
	
	private String visitTypeName;
	
	private String bloodGroupName;
	
	private String uhidNumber;
	
	private String bloodBagNo;
	
	private Integer bloodBankStockId;
	
	private Integer crossMatchRegisterDetailsId;
	
	private Integer requisitionTypeId;
	
	private String userName;

	private String compairIssueDate;
	
	private String requisitionNumber;
	
	private List<CrossMatchRegisterDetailsDto> crossMatchRegisterDetails;

	public Integer getCrossMatchRegisterId() {
		return crossMatchRegisterId;
	}

	public void setCrossMatchRegisterId(Integer crossMatchRegisterId) {
		this.crossMatchRegisterId = crossMatchRegisterId;
	}

	public Integer getPatientRequisitionId() {
		return patientRequisitionId;
	}

	public void setPatientRequisitionId(Integer patientRequisitionId) {
		this.patientRequisitionId = patientRequisitionId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Integer getRequiredBloodGroup() {
		return requiredBloodGroup;
	}

	public void setRequiredBloodGroup(Integer requiredBloodGroup) {
		this.requiredBloodGroup = requiredBloodGroup;
	}

	public Integer getPatientBloodGroup() {
		return patientBloodGroup;
	}

	public void setPatientBloodGroup(Integer patientBloodGroup) {
		this.patientBloodGroup = patientBloodGroup;
	}

	public Integer getRequiredQty() {
		return requiredQty;
	}

	public void setRequiredQty(Integer requiredQty) {
		this.requiredQty = requiredQty;
	}

	public Integer getPendingQty() {
		return pendingQty;
	}

	public void setPendingQty(Integer pendingQty) {
		this.pendingQty = pendingQty;
	}

	public Integer getBtoId() {
		return btoId;
	}

	public void setBtoId(Integer btoId) {
		this.btoId = btoId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCrossMatchStatus() {
		return crossMatchStatus;
	}

	public void setCrossMatchStatus(Integer crossMatchStatus) {
		this.crossMatchStatus = crossMatchStatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public List<CrossMatchRegisterDetailsDto> getCrossMatchRegisterDetails() {
		return crossMatchRegisterDetails;
	}

	public void setCrossMatchRegisterDetails(List<CrossMatchRegisterDetailsDto> crossMatchRegisterDetails) {
		this.crossMatchRegisterDetails = crossMatchRegisterDetails;
	}

	public String getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(String requisitionDate) {
		this.requisitionDate = requisitionDate;
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

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}

	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getBloodGroupName() {
		return bloodGroupName;
	}

	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public Integer getCrossMatchRegisterDetailsId() {
		return crossMatchRegisterDetailsId;
	}

	public void setCrossMatchRegisterDetailsId(Integer crossMatchRegisterDetailsId) {
		this.crossMatchRegisterDetailsId = crossMatchRegisterDetailsId;
	}

	public Integer getRequisitionTypeId() {
		return requisitionTypeId;
	}

	public void setRequisitionTypeId(Integer requisitionTypeId) {
		this.requisitionTypeId = requisitionTypeId;
	}

	public String getSegmentNo() {
		return segmentNo;
	}

	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompairIssueDate() {
		return compairIssueDate;
	}

	public void setCompairIssueDate(String compairIssueDate) {
		this.compairIssueDate = compairIssueDate;
	}

	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}

	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	
}
