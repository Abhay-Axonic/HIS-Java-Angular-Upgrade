package com.param.pharmacy.nursing.discharge.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MedicationReconciliationResponse {

	private Long id;
	
    private String uhid;
    
    private Integer patientId;
    
    private String patientDetail;
    
    private Date dob;
	
    private Integer doctorId;
    
	private String doctorDetail;
    
    private Integer wardId;
    
    private String wardName;
    
    private Integer bedId;
    
    private String bedNo;
    
	private Date doa;
	
	private Date pdd;
	
	private Integer genderId;
	
	private String imagePath;
	
    private List<MedicationReconciliationItem> items;
    private Integer statusId;
    
	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public MedicationReconciliationResponse() {
		super();
	}

	public MedicationReconciliationResponse(Long id, String uhid, Integer patientId, String patientDetail, Date dob,
			Integer doctorId, String doctorDetail) {
		super();
		this.id = id;
		this.uhid = uhid;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public List<MedicationReconciliationItem> getItems() {
		return items;
	}

	public void setItems(List<MedicationReconciliationItem> items) {
		this.items = items;
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
    
}
