package com.param.procurement.consignment.dto.patientConsumption;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientConsumtionDetail {

	private Integer id;

	private String toStore;

	private Integer storeId;

	private Integer visitTypeId;

	private String visitType;

	private String patientName;

	private String uhid;

	private String wardName;

	private String bedNo;

	private String visitNo;

	private String indentNo;

	private Integer admissionId;

	private Integer encounterId;

	private Integer doctorId;

	private Integer statusId;

	private String status;

	private Integer patientIndentId;

	private Integer implantIndentId;
	
	private List<PatientConsumtionDetailtem> items;

	private List<Carrier> carrier;
	
	
	public PatientConsumtionDetail() {
		super();
	}
	
    public PatientConsumtionDetail(Integer id, String toStore, Integer storeId, Integer visitTypeId,
			String visitType, String patientName, String uhid, String visitNo, String indentNo, Integer doctorId) {
		super();
		this.id = id;
		this.toStore = toStore;
		this.storeId = storeId;
		this.visitTypeId = visitTypeId;
		this.visitType = visitType;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitNo = visitNo;
		this.indentNo = indentNo;
		this.doctorId = doctorId;
	}

	public PatientConsumtionDetail(Integer id, String toStore, Integer storeId, Integer visitTypeId,
	String visitType, String patientName, String uhid, String visitNo, String indentNo, Integer doctorId,
	Integer patientIndentId)
		{
				this(id, toStore, storeId, visitTypeId, visitType, patientName, uhid, visitNo, indentNo, doctorId);
				this.patientIndentId = patientIndentId;
		}
	
	public PatientConsumtionDetail(Integer id, Integer implantIndentId, String toStore, Integer storeId, Integer visitTypeId,
			String visitType, String patientName, String uhid, String visitNo, String indentNo, Integer doctorId
			) {
		this(id, toStore, storeId, visitTypeId, visitType, patientName, uhid, visitNo, indentNo, doctorId);
		this.implantIndentId = implantIndentId;
	}
  
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public List<PatientConsumtionDetailtem> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumtionDetailtem> items) {
		this.items = items;
	}

	public List<Carrier> getCarrier() {
		return carrier;
	}

	public void setCarrier(List<Carrier> carrier) {
		this.carrier = carrier;
	}

}
