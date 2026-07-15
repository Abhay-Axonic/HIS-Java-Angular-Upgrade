package com.param.procedure.dto;

import java.util.List;

import com.param.global.otims.dto.ProcedureOrderMasterDto;

public class ProcedureRequestDto {

	
	private Integer procedureOrderId;
	private Integer primaryProcedureId;
	private Integer serviceId;
	private String preferedDateTime;
	private String requestDate;
	private Integer orgId;
	private Integer unitId;
	private Integer patientId;
	private Integer admissionId;
	private Integer encounterId;
	private Integer requestBy;
	private List<ProcedureOrderMasterDto> procedureOrderMasterDto;
	private List<VOProcedureOrderMasterDto> ProcedureOrderMaster;
	private char isBloodComponentRequired;
	private String uhid;
	private String patientName;
	private String procedureName;
	private Character isPrimarySecondary;
	private Integer proReqId;
	private String procedureType;
	private Integer specialityId;
	private String specialityName;
	private String admintionDate;
	private String fromDate;
	private String toDate;
	private Integer visitTypeId;
	private String preferedTime;
	private String pdd;
	private String doa;
	private String dob;
	private String doctorName;
	private String bedNumber;
	private String wardName;
	private String age;
	private Integer genderId;
	private String imagePath;
	private String genderCode;
	
	public String getProcedureType() {
		return procedureType;
	}
	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}
	public Integer getProReqId() {
		return proReqId;
	}
	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}
	public List<ProcedureOrderMasterDto> getProcedureOrderMasterDto() {
		return procedureOrderMasterDto;
	}
	public void setProcedureOrderMasterDto(List<ProcedureOrderMasterDto> procedureOrderMasterDto) {
		this.procedureOrderMasterDto = procedureOrderMasterDto;
	}
	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}
	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}
	public Integer getPrimaryProcedureId() {
		return primaryProcedureId;
	}
	public void setPrimaryProcedureId(Integer primaryProcedureId) {
		this.primaryProcedureId = primaryProcedureId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getPreferedDateTime() {
		return preferedDateTime;
	}
	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public Integer getRequestBy() {
		return requestBy;
	}
	public void setRequestBy(Integer requestBy) {
		this.requestBy = requestBy;
	}
	public char getIsBloodComponentRequired() {
		return isBloodComponentRequired;
	}
	public void setIsBloodComponentRequired(char isBloodComponentRequired) {
		this.isBloodComponentRequired = isBloodComponentRequired;
	}
	public String getUhid() {
		return uhid;
	}
	public void setUhid(String uhid) {
		this.uhid = uhid;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public Character getIsPrimarySecondary() {
		return isPrimarySecondary;
	}
	public void setIsPrimarySecondary(Character isPrimarySecondary) {
		this.isPrimarySecondary = isPrimarySecondary;
	}
	public List<VOProcedureOrderMasterDto> getProcedureOrderMaster() {
		return ProcedureOrderMaster;
	}
	public void setProcedureOrderMaster(List<VOProcedureOrderMasterDto> procedureOrderMaster) {
		ProcedureOrderMaster = procedureOrderMaster;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getAdmintionDate() {
		return admintionDate;
	}
	public void setAdmintionDate(String admintionDate) {
		this.admintionDate = admintionDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public String getPreferedTime() {
		return preferedTime;
	}
	public void setPreferedTime(String preferedTime) {
		this.preferedTime = preferedTime;
	}
	public String getPdd() {
		return pdd;
	}
	public void setPdd(String pdd) {
		this.pdd = pdd;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
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
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	
	
}
