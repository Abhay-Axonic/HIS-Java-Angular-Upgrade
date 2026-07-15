package com.param.procurement.consignment.dto.patientConsumption;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientConsumptionResponse {

	protected Integer id;

	protected String consumptionNo;
	
	protected String issueNo;

	protected Date consumptionDate;

	protected Integer visitTypeId;

	protected String visitType;

	protected String visitNo;

	protected Integer admissionId;
	
	protected Integer encounterId;

	protected String uhid;

	protected String issuedBy;

	protected String patientDetail;

	protected String doctorDetail;

	protected Integer storeId;

	protected String storeName;

	protected Integer statusId;

	protected String status;

	protected String patientName;
	
	protected Integer patientIndentId;

	protected Integer implantIndentId;
	
	protected String wardName;

	protected String bedNo;
	
	protected String indentNo;

	protected Date indentDate;
	
	private Integer doctorId;

	private List<PatientConsumptionItemResponse> items;
	
	

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public PatientConsumptionResponse() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConsumptionNo() {
		return consumptionNo;
	}

	public void setConsumptionNo(String consumptionNo) {
		this.consumptionNo = consumptionNo;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public List<PatientConsumptionItemResponse> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumptionItemResponse> items) {
		this.items = items;
	}

	public PatientConsumptionResponse(Integer id, String consumptionNo, Date consumptionDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName, Integer statusId,
			String status, String issuedBy,String issueNo) {
		super();
		this.id = id;
		this.consumptionNo = consumptionNo;
		this.consumptionDate = consumptionDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeName = storeName;
		this.statusId = statusId;
		this.status = status;
		this.issuedBy = issuedBy;
		this.issueNo= issueNo;
	}

	public PatientConsumptionResponse(Integer id, String consumptionNo, Integer visitTypeId, String visitType,
			String visitNo, String uhid, Integer storeId, String storeName, String patientName) {
		super();
		this.id = id;
		this.consumptionNo = consumptionNo;
		this.visitTypeId = visitTypeId;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.storeId = storeId;
		this.storeName = storeName;
		this.patientName = patientName;
	}
	public PatientConsumptionResponse(Integer id, String storeName, Integer storeId, Integer visitTypeId,
			String visitType, String patientName, String uhid, String visitNo, String indentNo,
			Integer doctorId, Integer patientIndentId)
	{
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeId = storeId;
		this.visitTypeId = visitTypeId;
		this.visitType = visitType;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitNo = visitNo;
		this.indentNo=indentNo;
		this.doctorId=doctorId;
		this.patientIndentId=patientIndentId;
		
	}

	public PatientConsumptionResponse(Integer id, String storeName, Integer storeId, Integer visitTypeId,
			String visitType, String patientName, String uhid, String visitNo, String indentNo,
			Integer doctorId) 
	{
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeId = storeId;
		this.visitTypeId = visitTypeId;
		this.visitType = visitType;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitNo=visitNo;
		this.indentNo=indentNo;
		this.doctorId=doctorId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientIndentId() {
		return patientIndentId;
	}

	public void setPatientIndentId(Integer patientIndentId) {
		this.patientIndentId = patientIndentId;
	}

	public Integer getImplantIndentId() {
		return implantIndentId;
	}

	public void setImplantIndentId(Integer implantIndentId) {
		this.implantIndentId = implantIndentId;
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

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
}
