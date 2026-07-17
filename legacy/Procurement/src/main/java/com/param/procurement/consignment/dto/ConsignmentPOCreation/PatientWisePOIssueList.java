package com.param.procurement.consignment.dto.ConsignmentPOCreation;

import java.util.Date;

import com.param.global.dto.IssueAcceptanceDto;

public class PatientWisePOIssueList {
	
	private String issueNo;
	
	private Date issueDate;
	
	private Integer visitTypeId;
	
	private String visitType;
	
	private String visitNo;
	
	private String patientDetails;
	
	private String issueItemCode;
	
	private String batchNo;
	
	private String issueItemName;
	
	private Long batchId;
	
	private Date issueExpiry;
	
	private Integer issueQty;
	
	private Double issueCop;
	
	private Double issueMrp;
	
	private String issueTaxCode;
	
	private Double  issueTaxPercentage;
	
	private Double  issueNetAmount;
	
	private Integer patientId;
	
	private Integer issueId;
	
	

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public PatientWisePOIssueList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PatientWisePOIssueList(String issueNo, Date issueDate, Integer visitTypeId, String visitType,
			String visitNo, String patientDetails, String issueItemCode, String issueItemName, Long batchId,
			String batchNo, Date issueExpiry, Integer issueQty, Double issueCop, Double issueMrp,
			String issueTaxCode, Double issueTaxPercentage, Double issueNetAmount,Integer patientId,Integer issueId) {
		this.issueNo=issueNo;
		this.issueDate=issueDate;
		this.visitTypeId=visitTypeId;
		this.visitType=visitType;
		this.visitNo=visitNo;
		this.patientDetails=patientDetails;
		this.issueItemCode=issueItemCode;
		this.issueItemName=issueItemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.issueExpiry=issueExpiry;
		this.issueQty=issueQty;
		this.issueCop=issueCop;
		this.issueMrp=issueMrp;
		this.issueTaxCode=issueTaxCode;
		this.issueTaxPercentage=issueTaxPercentage;
		this.issueNetAmount=issueNetAmount;
		this.patientId=patientId;
		this.issueId=issueId;
	}

	

	public String getIssueItemName() {
		return issueItemName;
	}

	public void setIssueItemName(String issueItemName) {
		this.issueItemName = issueItemName;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getIssueItemCode() {
		return issueItemCode;
	}

	public void setIssueItemCode(String issueItemCode) {
		this.issueItemCode = issueItemCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Date getIssueExpiry() {
		return issueExpiry;
	}

	public void setIssueExpiry(Date issueExpiry) {
		this.issueExpiry = issueExpiry;
	}

	public Integer getIssueQty() {
		return issueQty;
	}

	public void setIssueQty(Integer issueQty) {
		this.issueQty = issueQty;
	}

	public Double getIssueCop() {
		return issueCop;
	}

	public void setIssueCop(Double issueCop) {
		this.issueCop = issueCop;
	}

	public Double getIssueMrp() {
		return issueMrp;
	}

	public void setIssueMrp(Double issueMrp) {
		this.issueMrp = issueMrp;
	}

	public String getIssueTaxCode() {
		return issueTaxCode;
	}

	public void setIssueTaxCode(String issueTaxCode) {
		this.issueTaxCode = issueTaxCode;
	}

	

	public Double getIssueTaxPercentage() {
		return issueTaxPercentage;
	}

	public void setIssueTaxPercentage(Double issueTaxPercentage) {
		this.issueTaxPercentage = issueTaxPercentage;
	}

	public Double getIssueNetAmount() {
		return issueNetAmount;
	}

	public void setIssueNetAmount(Double issueNetAmount) {
		this.issueNetAmount = issueNetAmount;
	}

	
	
	
}
