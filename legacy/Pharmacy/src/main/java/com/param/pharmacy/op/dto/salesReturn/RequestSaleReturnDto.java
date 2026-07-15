package com.param.pharmacy.op.dto.salesReturn;

import java.util.List;

public class RequestSaleReturnDto {
	private Integer storeId;

	private Integer statusId;

	private Long saleId;
	
	private List<Long> saleIds;
	
	private List<RequestSaleReturnSave> reuquestSaleReturnList;	

	private List<RequestSaleReturnSaveItem> item;

	private Double totalRefundAmount;
	
	private String validationMessage;

	private Double totalreturnPaidAmount;	
	
	private Integer patientId;
	
	private String uhidNumber;
	
	private String prefixCode;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String dob;
	
	private String genderCode;
	
	private String mobileNumber;
	private Integer unitId;
	private String patientName;
	private String details;
	private String uhid;

	private List<Integer>statusIdsList;

	
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	public List<Long> getSaleIds() {
		return saleIds;
	}
	public void setSaleIds(List<Long> saleIds) {
		this.saleIds = saleIds;
	}
	public List<RequestSaleReturnSave> getReuquestSaleReturnList() {
		return reuquestSaleReturnList;
	}
	public void setReuquestSaleReturnList(List<RequestSaleReturnSave> reuquestSaleReturnList) {
		this.reuquestSaleReturnList = reuquestSaleReturnList;
	}
	public List<RequestSaleReturnSaveItem> getItem() {
		return item;
	}
	public void setItem(List<RequestSaleReturnSaveItem> item) {
		this.item = item;
	}
	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}
	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
	public String getValidationMessage() {
		return validationMessage;
	}
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	public Double getTotalreturnPaidAmount() {
		return totalreturnPaidAmount;
	}
	public void setTotalreturnPaidAmount(Double totalreturnPaidAmount) {
		this.totalreturnPaidAmount = totalreturnPaidAmount;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getPrefixCode() {
		return prefixCode;
	}
	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public List<Integer> getStatusIdsList() {
		return statusIdsList;
	}
	public void setStatusIdsList(List<Integer> statusIdsList) {
		this.statusIdsList = statusIdsList;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getUhid() {
		return uhid;
	}
	public void setUhid(String uhid) {
		this.uhid = uhid;
	}


}
