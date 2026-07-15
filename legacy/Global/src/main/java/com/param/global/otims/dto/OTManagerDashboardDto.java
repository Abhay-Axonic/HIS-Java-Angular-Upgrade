package com.param.global.otims.dto;

import java.util.List;

public class OTManagerDashboardDto {
	
	private Integer id;
	
	private Integer patientId;
	
	private Integer doctorId;
	
	private Integer createdBy;
	
	private Integer specialityId;
	
	private String uhidNumber;
	
	private String doctorName;
	
	private String patientName;
	
	private Integer procedureOrderId;
	
	private String genderName;
	
	private String specialityName;
	
	private String age;
	
	private String procedureName;
	
	private String priority;
	
	private Integer otId;
	
	private Integer itemId;
	
	private String itemName;
	
	private String procedureDate;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer urgency;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer isImplant;
	
	private Integer implantDetailsId;
	
	private Integer isKit;
	
	private Integer visitTypeId;
	
	private Integer kitId;
	
	private String kitName;
	
	private Integer ward;
	
	private Integer genderId;
	
	private String genderCode;
	
	private String imagePath;
	
	private String preferTime;
	
	private String pdd;
	
	private String doa;
	
	private String bedNumber;
	
	private String birthDate;
	
	private String wardName;
	
	private List<MedicationDetailsDto> medicationList;
	
	private List<ImplantDetailsDto> implantList;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private Integer bed;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getOtId() {
		return otId;
	}

	public void setOtId(Integer otId) {
		this.otId = otId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProcedureDate() {
		return procedureDate;
	}

	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public Integer getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Integer isImplant) {
		this.isImplant = isImplant;
	}

	public Integer getImplantDetailsId() {
		return implantDetailsId;
	}

	public void setImplantDetailsId(Integer implantDetailsId) {
		this.implantDetailsId = implantDetailsId;
	}

	public Integer getIsKit() {
		return isKit;
	}

	public void setIsKit(Integer isKit) {
		this.isKit = isKit;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	public Integer getBed() {
		return bed;
	}

	public void setBed(Integer bed) {
		this.bed = bed;
	}

	public List<MedicationDetailsDto> getMedicationList() {
		return medicationList;
	}

	public void setMedicationList(List<MedicationDetailsDto> medicationList) {
		this.medicationList = medicationList;
	}

	public List<ImplantDetailsDto> getImplantList() {
		return implantList;
	}

	public void setImplantList(List<ImplantDetailsDto> implantList) {
		this.implantList = implantList;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPreferTime() {
		return preferTime;
	}

	public void setPreferTime(String preferTime) {
		this.preferTime = preferTime;
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

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	
	
}
