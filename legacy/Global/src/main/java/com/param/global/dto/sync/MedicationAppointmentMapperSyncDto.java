package com.param.global.dto.sync;

public class MedicationAppointmentMapperSyncDto {

	private Integer appointmentId;
	private Integer hospitalId;
	private Integer unitId;
	private Integer globalUhid;
	private String genericName;
	private String medicationName;
	private String startDate;
	private String endDate;
	private String frequency;
	private String remark;
	private char historyFlag;
	private String localUhid;
	private Integer pkMedicationAppointmentMapperId;
	private Integer applicationId;
	private Integer admissionId;

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	public Integer getPkMedicationAppointmentMapperId() {
		return pkMedicationAppointmentMapperId;
	}

	public void setPkMedicationAppointmentMapperId(Integer pkMedicationAppointmentMapperId) {
		this.pkMedicationAppointmentMapperId = pkMedicationAppointmentMapperId;
	}

	public String getLocalUhid() {
		return localUhid;
	}

	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}

	private char status;
	private String organisationName;

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

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

	public Integer getGlobalUhid() {
		return globalUhid;
	}

	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public char getHistoryFlag() {
		return historyFlag;
	}

	public void setHistoryFlag(char historyFlag) {
		this.historyFlag = historyFlag;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	
	
}
