package com.param.service.dto;

import java.util.List;


public class BillFundSanctionDetailsDto {

	private Integer fundsanctionrequestApprovalDetailsId; 	
	
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
	
	private Double requestApprovalAmount;
	
	private Integer fundAcceptacepaymentTypeId;
	
	private String fundrequestApprovalDate;

	private String status;
	
	private Integer fundInFlowId;
	
	private Double approvalBillConsumeAmount;
	
	private Double virtualPaidAmount;
	
	private Double  virtualAcceptancePaidAmmount;
	
	private Boolean billsanctionApprovedflagforCheck ;
	
	private Double billAcceptancePaidAmount;
	
	private Integer billingMasterId;
	
	private Integer  billFundconsumptionTypeId ;
	
	private Double selectedFundAmount;
	
	private Double billpayableAmount;
	
	private Integer billFundsanctionDetailId;
	
	
	private Integer billFundDetailsId;
	
	private Integer fundInflowVirtualApprovalDetailsId;
	
	
	
	
	
	private List<FundInFlowDetailsDto> fundInFlowDetailsDto;
	
	private List<BillFundSanctionDetailsDto> billFundSanctionDetailsDto; 
	

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

	public Double getRequestApprovalAmount() {
		return requestApprovalAmount;
	}

	public void setRequestApprovalAmount(Double requestApprovalAmount) {
		this.requestApprovalAmount = requestApprovalAmount;
	}

	public Integer getFundAcceptacepaymentTypeId() {
		return fundAcceptacepaymentTypeId;
	}

	public void setFundAcceptacepaymentTypeId(Integer fundAcceptacepaymentTypeId) {
		this.fundAcceptacepaymentTypeId = fundAcceptacepaymentTypeId;
	}

	public List<FundInFlowDetailsDto> getFundInFlowDetailsDto() {
		return fundInFlowDetailsDto;
	}

	public void setFundInFlowDetailsDto(List<FundInFlowDetailsDto> fundInFlowDetailsDto) {
		this.fundInFlowDetailsDto = fundInFlowDetailsDto;
	}

	public void setFundrequestDate(String fundrequestDate) {
		this.fundrequestDate = fundrequestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public String getFundrequestApprovalDate() {
		return fundrequestApprovalDate;
	}

	public void setFundrequestApprovalDate(String fundrequestApprovalDate) {
		this.fundrequestApprovalDate = fundrequestApprovalDate;
	}

	public Integer getFundInFlowId() {
		return fundInFlowId;
	}

	public void setFundInFlowId(Integer fundInFlowId) {
		this.fundInFlowId = fundInFlowId;
	}

	public Double getApprovalBillConsumeAmount() {
		return approvalBillConsumeAmount;
	}

	public void setApprovalBillConsumeAmount(Double approvalBillConsumeAmount) {
		this.approvalBillConsumeAmount = approvalBillConsumeAmount;
	}

	public Double getVirtualPaidAmount() {
		return virtualPaidAmount;
	}

	public void setVirtualPaidAmount(Double virtualPaidAmount) {
		this.virtualPaidAmount = virtualPaidAmount;
	}

	public Double getVirtualAcceptancePaidAmmount() {
		return virtualAcceptancePaidAmmount;
	}

	public void setVirtualAcceptancePaidAmmount(Double virtualAcceptancePaidAmmount) {
		this.virtualAcceptancePaidAmmount = virtualAcceptancePaidAmmount;
	}

	public List<BillFundSanctionDetailsDto> getBillFundSanctionDetailsDto() {
		return billFundSanctionDetailsDto;
	}

	public void setBillFundSanctionDetailsDto(List<BillFundSanctionDetailsDto> billFundSanctionDetailsDto) {
		this.billFundSanctionDetailsDto = billFundSanctionDetailsDto;
	}

	public Boolean getBillsanctionApprovedflagforCheck() {
		return billsanctionApprovedflagforCheck;
	}

	public void setBillsanctionApprovedflagforCheck(Boolean billsanctionApprovedflagforCheck) {
		this.billsanctionApprovedflagforCheck = billsanctionApprovedflagforCheck;
	}

	public Double getBillAcceptancePaidAmount() {
		return billAcceptancePaidAmount;
	}

	public void setBillAcceptancePaidAmount(Double billAcceptancePaidAmount) {
		this.billAcceptancePaidAmount = billAcceptancePaidAmount;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getBillFundconsumptionTypeId() {
		return billFundconsumptionTypeId;
	}

	public void setBillFundconsumptionTypeId(Integer billFundconsumptionTypeId) {
		this.billFundconsumptionTypeId = billFundconsumptionTypeId;
	}

	public Double getSelectedFundAmount() {
		return selectedFundAmount;
	}

	public void setSelectedFundAmount(Double selectedFundAmount) {
		this.selectedFundAmount = selectedFundAmount;
	}

	public Double getBillpayableAmount() {
		return billpayableAmount;
	}

	public void setBillpayableAmount(Double billpayableAmount) {
		this.billpayableAmount = billpayableAmount;
	}

	public Integer getBillFundsanctionDetailId() {
		return billFundsanctionDetailId;
	}

	public void setBillFundsanctionDetailId(Integer billFundsanctionDetailId) {
		this.billFundsanctionDetailId = billFundsanctionDetailId;
	}

	public Integer getBillFundDetailsId() {
		return billFundDetailsId;
	}

	public void setBillFundDetailsId(Integer billFundDetailsId) {
		this.billFundDetailsId = billFundDetailsId;
	}

	public Integer getFundInflowVirtualApprovalDetailsId() {
		return fundInflowVirtualApprovalDetailsId;
	}

	public void setFundInflowVirtualApprovalDetailsId(Integer fundInflowVirtualApprovalDetailsId) {
		this.fundInflowVirtualApprovalDetailsId = fundInflowVirtualApprovalDetailsId;
	}

}
