package com.param.service.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.global.VisitType;

public class FundSanctionRequestDto {

			
	private Integer fundsanctionrequestDetailsId;	
	
	private String  fundrequestNumber;
	
	private Integer trustId;
	
	private String trustName;
	
	private Double fundrequestAmount;
	
    private String createdDate;
	
	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
    private Integer unitId;
	
	private Integer orgId;
	
	private String fundrequestRemark;
	
	private char sanctionReqStatus;
	
	private Integer patientId;
	
	private String patientName;
	
	private String recieptNumber;
	
	private String uhidNumber;
	
	private String toDate;
	
	private String fromDate;

	private Integer fundrequestStatusId;
	
	private String fundrequestDate;
	
	private Integer fundsanctionrequestApprovalDetailsId;

	public Integer getFundsanctionrequestDetailsId() {
		return fundsanctionrequestDetailsId;
	}

	public void setFundsanctionrequestDetailsId(Integer fundsanctionrequestDetailsId) {
		this.fundsanctionrequestDetailsId = fundsanctionrequestDetailsId;
	}

	public String getFundrequestNumber() {
		return fundrequestNumber;
	}

	public void setFundrequestNumber(String fundrequestNumber) {
		this.fundrequestNumber = fundrequestNumber;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public String getTrustName() {
		return trustName;
	}

	public void setTrustName(String trustName) {
		this.trustName = trustName;
	}

	public Double getFundrequestAmount() {
		return fundrequestAmount;
	}

	public void setFundrequestAmount(Double fundrequestAmount) {
		this.fundrequestAmount = fundrequestAmount;
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

	public String getFundrequestRemark() {
		return fundrequestRemark;
	}

	public void setFundrequestRemark(String fundrequestRemark) {
		this.fundrequestRemark = fundrequestRemark;
	}

	public char getSanctionReqStatus() {
		return sanctionReqStatus;
	}

	public void setSanctionReqStatus(char sanctionReqStatus) {
		this.sanctionReqStatus = sanctionReqStatus;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getFundrequestStatusId() {
		return fundrequestStatusId;
	}

	public void setFundrequestStatusId(Integer fundrequestStatusId) {
		this.fundrequestStatusId = fundrequestStatusId;
	}

	public String getFundrequestDate() {
		return fundrequestDate;
	}

	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public void setFundrequestDate(String fundrequestDate) {
		this.fundrequestDate = fundrequestDate;
	}

}
