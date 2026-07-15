package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DrugAdministrationResponse
{
	private Integer patientId;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private Integer visitTypeId;
	
	private String visitType;
	
	private Date dob;
	
	private Date doa;
	
	private Date lastOrderDate;
	
	private Date pdd;
	
	private String wardName;
	
	private String bedNo;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Long prescriptionId;
	
	private List<DrugAdministrationItem> items;

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Date getLastOrderDate() {
		return lastOrderDate;
	}

	public void setLastOrderDate(Date lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
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

	public List<DrugAdministrationItem> getItems() {
		return items;
	}

	public void setItems(List<DrugAdministrationItem> items) {
		this.items = items;
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

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public DrugAdministrationResponse() {
		super();
	}

	public DrugAdministrationResponse(Integer patientId, String uhid, String patientDetail, String doctorDetail,
			Date dob, Date lastOrderDate) {
		super();
		this.patientId = patientId;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.dob = dob;
		this.lastOrderDate = lastOrderDate;
	}
	
}