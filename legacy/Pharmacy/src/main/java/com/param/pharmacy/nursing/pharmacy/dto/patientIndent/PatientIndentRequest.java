package com.param.pharmacy.nursing.pharmacy.dto.patientIndent;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PatientIndentRequest extends CookieObjectDto
{
	 private Integer id;
	
	 private Date fromDate;
	 
	 private Date toDate;
	 
	 private String visitNo;
	 
	 private Integer visitType;
	 
	 private Integer store;

	 private Integer fromStoreId;

	 private Integer toStoreId;
	 
	 private Integer floor;
	 
	 private Integer ward;
	 
	 private String doctorName;
	 
	 private Integer patientId;
	 
	 private String patientName;
	 
	 private String remark;
	 
	 private String uhid;
	 
	 private Integer statusId;
	 
	 private Integer approvalStatusId;

	 private List<Integer> indentId;
	 
	 private Integer cancelReasonId;

	 private String cancelNote;
	 
	 private List<PatientIndentItem> items;
	 
	 private boolean consignment;
	 
	 private Integer doctorId;
	 
	 private List<Integer> wardList;
	 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<Integer> getIndentId() {
		return indentId;
	}

	public void setIndentId(List<Integer> indentId) {
		this.indentId = indentId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PatientIndentItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIndentItem> items) {
		this.items = items;
	}

	public Boolean getConsignment() {
		return consignment;
	}

	public void setConsignment(Boolean consignment) {
		this.consignment = consignment;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}
	
	

 }
