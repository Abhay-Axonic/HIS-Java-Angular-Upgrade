package com.param.adt.transfer.dto;

public class TransferOfCareDetailsByAdmDto {
	
	private Integer admissionId;
	private String fromSpecialityName;
	private String  fromDoctorName;
	private String toSpecialityName;
	private String toDoctorName;
	private String dt;
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public String getFromSpecialityName() {
		return fromSpecialityName;
	}
	public void setFromSpecialityName(String fromSpecialityName) {
		this.fromSpecialityName = fromSpecialityName;
	}
	public String getFromDoctorName() {
		return fromDoctorName;
	}
	public void setFromDoctorName(String fromDoctorName) {
		this.fromDoctorName = fromDoctorName;
	}
	public String getToSpecialityName() {
		return toSpecialityName;
	}
	public void setToSpecialityName(String toSpecialityName) {
		this.toSpecialityName = toSpecialityName;
	}
	public String getToDoctorName() {
		return toDoctorName;
	}
	public void setToDoctorName(String toDoctorName) {
		this.toDoctorName = toDoctorName;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	

}
