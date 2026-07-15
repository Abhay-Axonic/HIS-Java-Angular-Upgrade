package com.param.global.dto.sync;

public class AppointmentDoctorCoversheetMapperDto {
	private Integer appointmentId;
	private Integer hospitalId;
	private Integer unitId;
	private String doctorName;
	private String coversheetFilePath;
	private Integer globalUhid;
	
	private String organisationName;
	private String localUhid;
	private Integer pkAdcmId;
	private Integer applicationId;
	private String appointmentDate;
	private Integer encounterId;
	
	private byte[] filePath;
    private String coversheetMLFilePath;
    
	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getCoversheetFilePath() {
		return coversheetFilePath;
	}

	public void setCoversheetFilePath(String coversheetFilePath) {
		this.coversheetFilePath = coversheetFilePath;
	}

	public Integer getGlobalUhid() {
		return globalUhid;
	}

	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getLocalUhid() {
		return localUhid;
	}

	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}

	public Integer getPkAdcmId() {
		return pkAdcmId;
	}

	public void setPkAdcmId(Integer pkAdcmId) {
		this.pkAdcmId = pkAdcmId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public byte[] getFilePath() {
		return filePath;
	}

	public void setFilePath(byte[] filePath) {
		this.filePath = filePath;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getCoversheetMLFilePath() {
		return coversheetMLFilePath;
	}

	public void setCoversheetMLFilePath(String coversheetMLFilePath) {
		this.coversheetMLFilePath = coversheetMLFilePath;
	}
}
