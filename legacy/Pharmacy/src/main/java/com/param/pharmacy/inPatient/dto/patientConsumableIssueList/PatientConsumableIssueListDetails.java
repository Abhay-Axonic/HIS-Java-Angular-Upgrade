package com.param.pharmacy.inPatient.dto.patientConsumableIssueList;

import java.util.List;

import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueItem;

public class PatientConsumableIssueListDetails
{
	private Integer id;
	
	private String toStore;
	
	private Integer storeId;

	private String issuingStore;

	private Integer issuingStoreId;
	
	private String visitType;
	
	private String patientName;
	
	private String uhid;
	
	private String wardName;
	
	private String bedNo;
	
	private String visitNo;
	
	private String indentNo;
	
	private Integer carrierId;
	
	private String carrierName;
	
	private Integer statusId;
	
	private String status;
	
	private String remark;
	
	private List<PatientConsumableIssueItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
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

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PatientConsumableIssueItem> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumableIssueItem> items) {
		this.items = items;
	}

	public String getIssuingStore() {
		return issuingStore;
	}

	public void setIssuingStore(String issuingStore) {
		this.issuingStore = issuingStore;
	}

	public Integer getIssuingStoreId() {
		return issuingStoreId;
	}

	public void setIssuingStoreId(Integer issuingStoreId) {
		this.issuingStoreId = issuingStoreId;
	}

	public PatientConsumableIssueListDetails()
	{
		super();
	}
	
	public PatientConsumableIssueListDetails(Integer id, String toStore, Integer storeId,
											 String issuingStore,Integer issuingStoreId, String visitType,
			String patientName, String uhid, String visitNo, String indentNo, Integer carrierId, String carrierName) {
		super();
		this.id = id;
		this.toStore = toStore;
		this.storeId = storeId;
		this.issuingStore = issuingStore;
		this.issuingStoreId = issuingStoreId;
		this.visitType = visitType;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitNo = visitNo;
		this.indentNo = indentNo;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
	}
	
}