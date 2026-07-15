package com.param.pharmacy.nursing.crashCart.dto;

import java.util.Date;
import java.util.List;

import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.dto.global.CommonSearchRequest;

public class CrashCartRequest extends PharmacyBillCommonDto {

	//protected Integer id;

	//private Integer doctorId;

	private String visitNo;

	//private Integer storeId;

	private Integer bedId;

	private String crashCartNo;

	private String codeBlueDateTime;

	//private Integer admissionId;

	//private Integer encounterId;

	private Integer statusId;

	private Integer approvedStatusId;

	//private String remark;

	private List<ItemRequest> items;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
private Date fromDate;
	
	private Date toDate;
	
	private Integer visitType;
	
	//private Integer patientId;
	
	private String patientName;
	
	private String uhid;
	
	private Integer wardId;
	
	private String doctorName;
	
	private List<Integer> wardList;

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
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

	/*public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}*/

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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	/*public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}*/

	/*public Integer getDoctorId() {
		return doctorId;
	}
	
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}*/

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	/*public Integer getStoreId() {
		return storeId;
	}
	
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}*/

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public String getCrashCartNo() {
		return crashCartNo;
	}

	public void setCrashCartNo(String crashCartNo) {
		this.crashCartNo = crashCartNo;
	}

	public String getCodeBlueDateTime() {
		return codeBlueDateTime;
	}

	public void setCodeBlueDateTime(String codeBlueDateTime) {
		this.codeBlueDateTime = codeBlueDateTime;
	}

	/*public Integer getAdmissionId() {
		return admissionId;
	}
	
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}*/

	/*public Integer getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}*/

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovedStatusId() {
		return approvedStatusId;
	}

	public void setApprovedStatusId(Integer approvedStatusId) {
		this.approvedStatusId = approvedStatusId;
	}

	/*public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}*/

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
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

}
