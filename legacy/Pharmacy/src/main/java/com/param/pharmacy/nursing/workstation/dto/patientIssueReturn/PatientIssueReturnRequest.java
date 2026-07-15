package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PatientIssueReturnRequest extends CookieObjectDto
{
	 private Date fromDate;
	 
	 private Date toDate;
	 private Date expireDate;
	 
	 public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	private Integer visitType;
	
	 private Integer patientId;
	 private Integer statusId;
	

	private String patientName;

	 private String uhid;
		
	private Integer wardId;

	private String returnNo;
	
	private String visitNo;
	
	private Integer storeId;
	private Integer toStoreId;
	private Integer A;
	private Integer B;
	private Integer abc;
	private Integer xyz;
	private Integer id;
	private Boolean isActive;
	private Boolean isDeleted;
	private Integer organizationId;
	private Integer unitId;
	private Integer admissionId;
	private Integer assignPickerId;
	private Integer carrierId;
	private Double discount;
	private Integer doctorId;
	private Integer encounterId;
	private Integer implantIndentId;
	private Boolean isConsignment;
	private Boolean isImplant;
	private Boolean isNarcotics;
	private Boolean isReturn;
	private Boolean ongoing;
	private Double netAmount;
	private Double outstandingAmount;
	private Double paidAmount;
	private Integer patientIndentId;
	private Integer prescriptionId;
	private String remark;
	private Double roundOff;
	private Integer saleId;
	private Integer docSplId;
	private Double taxAmount;
	private Double totalAmount;
	private Double totalCreditPayable;
	private Double totalDiscountAmount;
	private Double totalSelfPayable;
	private Integer visitTypeId;
	private Timestamp patientDob;
	private String patientDetails;
	private String genderCode;
	private String uhidNumber;
	private Date createdDate;
	private String patientDetailsEnc;
	private String admissionNumber;
	private String storeName;
	private Integer issueDetailId;
	private Integer returnAcceptedQuantity;
	
	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	private String issueNo;
	
	private Integer itemId;

	private String itemName;
	
	private String itemCode;
	
	private String batchNo;
	
	private String bedNo;
	
	private List<Integer> wardList;

	private Boolean fromMedicineIssue;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
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

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getA() {
		return A;
	}

	public void setA(Integer a) {
		A = a;
	}

	public Integer getB() {
		return B;
	}

	public void setB(Integer b) {
		B = b;
	}

	public Integer getAbc() {
		return abc;
	}

	public void setAbc(Integer abc) {
		this.abc = abc;
	}

	public Integer getXyz() {
		return xyz;
	}

	public void setXyz(Integer xyz) {
		this.xyz = xyz;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getAssignPickerId() {
		return assignPickerId;
	}

	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getImplantIndentId() {
		return implantIndentId;
	}

	public void setImplantIndentId(Integer implantIndentId) {
		this.implantIndentId = implantIndentId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsNarcotics() {
		return isNarcotics;
	}

	public void setIsNarcotics(Boolean isNarcotics) {
		this.isNarcotics = isNarcotics;
	}

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public Boolean getOngoing() {
		return ongoing;
	}

	public void setOngoing(Boolean ongoing) {
		this.ongoing = ongoing;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Integer getPatientIndentId() {
		return patientIndentId;
	}

	public void setPatientIndentId(Integer patientIndentId) {
		this.patientIndentId = patientIndentId;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Integer getDocSplId() {
		return docSplId;
	}

	public void setDocSplId(Integer docSplId) {
		this.docSplId = docSplId;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}

	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}

	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}



	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPatientDetailsEnc() {
		return patientDetailsEnc;
	}

	public void setPatientDetailsEnc(String patientDetailsEnc) {
		this.patientDetailsEnc = patientDetailsEnc;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getReturnAcceptedQuantity() {
		return returnAcceptedQuantity;
	}

	public void setReturnAcceptedQuantity(Integer returnAcceptedQuantity) {
		this.returnAcceptedQuantity = returnAcceptedQuantity;
	}

	public Boolean getFromMedicineIssue() {
		return fromMedicineIssue;
	}

	public void setFromMedicineIssue(Boolean fromMedicineIssue) {
		this.fromMedicineIssue = fromMedicineIssue;
	}

}