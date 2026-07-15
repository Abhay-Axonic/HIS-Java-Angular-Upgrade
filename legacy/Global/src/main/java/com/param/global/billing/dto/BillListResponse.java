package com.param.global.billing.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;

@JsonInclude(Include.NON_NULL)
public class BillListResponse {
	protected Long id;

	protected Date billDate;

	protected String billNo;

	protected Integer patientId;
	
	protected String uhid;

	protected String patientDetails;
	
	protected Integer doctorId;
	
	protected String doctorName;
	
	protected Integer discountTypeId;	

	protected String salesType;

	protected Double totalAmount;
	
	protected Double discount;
	
	protected Double totalDiscount;
	
	protected Double taxAmount;
	
	protected Double roundOff;

	protected Double netAmount;

	protected Double paidAmt;

	protected Double balanceAmt;

	protected String status;
	
	protected String saleType;
	
	protected Integer saleTypeId;

	protected String orderBy;
	
	protected String discountCategory;
	
	protected Double refundAmt;
	
	protected List<ItemDetailsResponse> items;
	
	protected Timestamp patientDob;
	
	protected Integer tempPatId;
	
	protected String tempPatientDetails;
	
	protected Double totalCreditPayable;
	
	protected Double totalSelfPayable;
	
	protected Double selfDueAmount;
	
	protected boolean isConsolidatedBill;
	
	private Integer registrationTypeId;
	 private String patientName;
	    
	 private String ageString;
	    
	 private String genderCode;
	    
	 private Integer genderId;
		
	 private String imagePath;
	 
	 private Integer tempGenderId;
	 
	 private Integer admissionId;
	 
	 private Integer encounterId;
		
	 private List<PharamcyBillPayeeDetails> billPayeeList;

	 private Double concession;
	 private Double totalDiscountAmount;
	 private String tempUhid;
	 private String mobileNumber;
	 
	 private Integer statusId;
	 
	 protected Double creditDueAmount;
	 
	 private Boolean searchPatient;
	 private String tempPatientName;
	 private String countryCallingCode;
	 private boolean ipdOpdBillStatus;
	 private boolean pharmacyReceiptCancellation;
	 private boolean isPharmacyReturn;
	 private String invoiceGenerated;
	 private boolean isIssueReturn;
	 private Integer cancelledReasonId;
	 private String cancelledRemark;
	 private Character isInvoiceGenerated;
	public String getTempUhid() {
		return tempUhid;
	}

	public void setTempUhid(String tempUhid) {
		this.tempUhid = tempUhid;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public List<PharamcyBillPayeeDetails> getBillPayeeList() {
		return billPayeeList;
	}

	public void setBillPayeeList(List<PharamcyBillPayeeDetails> billPayeeList) {
		this.billPayeeList = billPayeeList;
	}

	public String getTempPatientDetails() {
		return tempPatientDetails;
	}

	public void setTempPatientDetails(String tempPatientDetails) {
		this.tempPatientDetails = tempPatientDetails;
	}

	public Integer getTempPatId() {
		return tempPatId;
	}

	public void setTempPatId(Integer tempPatId) {
		this.tempPatId = tempPatId;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public BillListResponse() {
		super();
	}

	public BillListResponse(Long id, Date billDate, String billNo, String salesType, Double totalAmount,
			Double netAmount, Double paidAmt, Double balanceAmt, String status, String orderBy, Double refundAmt, String doctorName) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billNo = billNo;
		this.salesType = salesType;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.paidAmt = paidAmt;
		this.balanceAmt = balanceAmt;
		this.status = status;
		this.orderBy = orderBy;
		this.refundAmt = refundAmt;
		this.doctorName=doctorName;
	}

	
	
	
	public BillListResponse(Long id, Date billDate, String billNo, Integer discountTypeId, String discountCategory,
			Double totalAmount, Double discount, Double totalDiscount, Double taxAmount, Double roundOff, Double netAmount,
			Double paidAmt, Double balanceAmt, String status, String saleType, Integer saleTypeId,Double totalCreditPayable,Double totalSelfPayable) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billNo = billNo;
		this.discountTypeId = discountTypeId;
		this.discountCategory = discountCategory;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.discount = discount;
		this.totalDiscount = totalDiscount;
		this.roundOff = roundOff;
		this.netAmount = netAmount;
		this.paidAmt = paidAmt;
		this.balanceAmt = balanceAmt;
		this.status = status;
		this.saleType = saleType;
		this.saleTypeId = saleTypeId;
		this.totalCreditPayable = totalCreditPayable;
		this.totalSelfPayable = totalSelfPayable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
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

	public Double getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public Double getBalanceAmt() {
		return balanceAmt;
	}

	public void setBalanceAmt(Double balanceAmt) {
		this.balanceAmt = balanceAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public List<ItemDetailsResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemDetailsResponse> items) {
		this.items = items;
	}
	
	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public void addItems(ItemDetailsResponse item) {
		if(getItems() == null) this.items = new ArrayList<ItemDetailsResponse>();
		getItems().add(item);
	}

	public void removeItems(ItemDetailsResponse item) {
		getItems().remove(item);
	}

	public String getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(String discountCategory) {
		this.discountCategory = discountCategory;
	}

	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}

	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}

	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}

	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}

	public boolean isConsolidatedBill() {
		return isConsolidatedBill;
	}

	public void setConsolidatedBill(boolean isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public Double getSelfDueAmount() {
		return selfDueAmount;
	}

	public void setSelfDueAmount(Double selfDueAmount) {
		this.selfDueAmount = selfDueAmount;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getTempGenderId() {
		return tempGenderId;
	}

	public void setTempGenderId(Integer tempGenderId) {
		this.tempGenderId = tempGenderId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getCreditDueAmount() {
		return creditDueAmount;
	}

	public void setCreditDueAmount(Double creditDueAmount) {
		this.creditDueAmount = creditDueAmount;
	}

	public Boolean getSearchPatient() {
		return searchPatient;
	}

	public void setSearchPatient(Boolean searchPatient) {
		this.searchPatient = searchPatient;
	}

	public String getTempPatientName() {
		return tempPatientName;
	}

	public void setTempPatientName(String tempPatientName) {
		this.tempPatientName = tempPatientName;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public boolean getIpdOpdBillStatus() {
		return ipdOpdBillStatus;
	}

	public void setIpdOpdBillStatus(boolean ipdOpdBillStatus) {
		this.ipdOpdBillStatus = ipdOpdBillStatus;
	}

	public boolean getPharmacyReceiptCancellation() {
		return pharmacyReceiptCancellation;
	}

	public void setPharmacyReceiptCancellation(boolean pharmacyReceiptCancellation) {
		this.pharmacyReceiptCancellation = pharmacyReceiptCancellation;
	}

	public boolean getIsPharmacyReturn() {
		return isPharmacyReturn;
	}

	public void setIsPharmacyReturn(boolean isPharmacyReturn) {
		this.isPharmacyReturn = isPharmacyReturn;
	}

	public String getInvoiceGenerated() {
		return invoiceGenerated;
	}

	public void setInvoiceGenerated(String invoiceGenerated) {
		this.invoiceGenerated = invoiceGenerated;
	}

	public boolean getIsIssueReturn() {
		return isIssueReturn;
	}

	public void setIsIssueReturn(boolean isIssueReturn) {
		this.isIssueReturn = isIssueReturn;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getCancelledRemark() {
		return cancelledRemark;
	}

	public void setCancelledRemark(String cancelledRemark) {
		this.cancelledRemark = cancelledRemark;
	}

	public Character getIsInvoiceGenerated() {
		return isInvoiceGenerated;
	}

	public void setIsInvoiceGenerated(Character isInvoiceGenerated) {
		this.isInvoiceGenerated = isInvoiceGenerated;
	}

	


}