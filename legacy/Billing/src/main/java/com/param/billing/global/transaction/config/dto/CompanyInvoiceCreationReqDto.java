package com.param.billing.global.transaction.config.dto;

import java.math.BigDecimal;

public class CompanyInvoiceCreationReqDto {
	private String doctorName;
	private String specialityName;
	private String  patientName;
	private String orderNo;
	private Long orderDate;
	private String serviceStandardName;
	private BigDecimal unitRate;
	private Double totalAmount;
	private Double netAmount;
	private Double billDateTime;
	private Double encounterDate;
	private String encounterNumber;
	private String billNumber;
	private Double discount;
	private Double concession;
	private Double totalBillAmount;
	private Double taxAmount;
	private Double taxPer;
	private String uhidNumber;
	private Integer orgUnitId;
	private Integer orgId;
	private Double  credtPayable;
	private Double selfPayable;
	private Integer quantity;
	private Integer payeeId;
	private Integer billPayeeDetailsId;
	private Integer billId;
	private Double dueAmount;
	private String fromDate;
	private String toDate;
	private Double creditBillAmount;
	private Integer invoiceId;
	private Double billAmount;
	private String visitNo;
	private String visitDate;
	private String invoiceDate;
	private String invoiceNumber;
	private String billDate;
	private Double receivedAmt;
	private Double tdsAmount;
	private Double outstanding;
	private boolean isPharamcy;
	private Double disallowedAmount;
	private Integer associateCompanyId;
	private Double otherDeduction;
	private Integer patientId;
	private Integer visitTypeId;
	private Integer billType;
	private Double crNetAmount;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}
	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Double getOtherDeduction() {
		return otherDeduction;
	}
	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
	public Double getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}
	public String getServiceStandardName() {
		return serviceStandardName;
	}
	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	public BigDecimal getUnitRate() {
		return unitRate;
	}
	public void setUnitRate(BigDecimal unitRate) {
		this.unitRate = unitRate;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getBillDateTime() {
		return billDateTime;
	}
	public void setBillDateTime(Double billDateTime) {
		this.billDateTime = billDateTime;
	}
	public Double getEncounterDate() {
		return encounterDate;
	}
	public void setEncounterDate(Double encounterDate) {
		this.encounterDate = encounterDate;
	}
	public String getEncounterNumber() {
		return encounterNumber;
	}
	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getConcession() {
		return concession;
	}
	public void setConcession(Double concession) {
		this.concession = concession;
	}
	public Double getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getTaxPer() {
		return taxPer;
	}
	public void setTaxPer(Double taxPer) {
		this.taxPer = taxPer;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public Integer getOrgUnitId() {
		return orgUnitId;
	}
	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Double getCredtPayable() {
		return credtPayable;
	}
	public void setCredtPayable(Double credtPayable) {
		this.credtPayable = credtPayable;
	}
	public Double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}
	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
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
	public Double getCreditBillAmount() {
		return creditBillAmount;
	}
	public void setCreditBillAmount(Double creditBillAmount) {
		this.creditBillAmount = creditBillAmount;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public Double getReceivedAmt() {
		return receivedAmt;
	}
	public void setReceivedAmt(Double receivedAmt) {
		this.receivedAmt = receivedAmt;
	}
	public Double getTdsAmount() {
		return tdsAmount;
	}
	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public boolean isPharamcy() {
		return isPharamcy;
	}
	public void setPharamcy(boolean isPharamcy) {
		this.isPharamcy = isPharamcy;
	}

		
}
