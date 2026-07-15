package com.param.bloodBank.dto;

import com.param.global.otims.dto.ProcedureOrderMasterDto;

import java.util.List;

public class BloodComponentOrderMasterDto {
	
    private Integer bloodComponentOrderId;
	
	private Integer bloodComponentId;
	
	private Integer unit;
	
	private String requisitionDate;
	
	private String requisitionOnDate;
	
	private Integer procedureOrderId;
	
	private String comments;
	
	private String instruction;
	
	private Integer priority;

	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Character isRequired;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;

	private Integer patientId;
	
	private String bloodComponentDesc;

	private String procedureDesc;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer requisitionNo;
	
	private String uhidNumber;
	
	private String patientName;
	
	private String dob;
	
	private String age;
	
	private String genderName;
	
	private String genderCode;
	
	private Integer genderId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private  Integer admissionId;
	
	private Integer visitTypeId;
	
	private String wardName;
	
	private String visitTypeName;
	
	private String bedNumber;
	private Boolean isExported;
	private Boolean isRequisitionClosed;
	private String issueDate;
	
	private String requisitionNumber;
	private String bloodBagIssueNo;
	
	private String userName;

	private List <BloodComponentOrderMasterDto> getListOfsavedComponents;
	private String addedDate;
	private List<String> dates;

	public List<BloodComponentOrderMasterDto> getGetListOfsavedComponents() {
		return getListOfsavedComponents;
	}

	public void setGetListOfsavedComponents(List<BloodComponentOrderMasterDto> getListOfsavedComponents) {
		this.getListOfsavedComponents = getListOfsavedComponents;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	private List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList;
	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}

	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(String requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public String getRequisitionOnDate() {
		return requisitionOnDate;
	}

	public void setRequisitionOnDate(String requisitionOnDate) {
		this.requisitionOnDate = requisitionOnDate;
	}

	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public List<BloodComponentOrderMasterDto> getBloodComponentOrderMasterDtoList() {
		return bloodComponentOrderMasterDtoList;
	}

	public void setBloodComponentOrderMasterDtoList(List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList) {
		this.bloodComponentOrderMasterDtoList = bloodComponentOrderMasterDtoList;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}

	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
	}

	public Character getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Character isRequired) {
		this.isRequired = isRequired;

	}
	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public String getProcedureDesc() {
		return procedureDesc;
	}

	public void setProcedureDesc(String procedureDesc) {
		this.procedureDesc = procedureDesc;
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

	public Integer getRequisitionNo() {
		return requisitionNo;
	}

	public void setRequisitionNo(Integer requisitionNo) {
		this.requisitionNo = requisitionNo;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Boolean getIsExported() {
		return isExported;
	}

	public void setIsExported(Boolean isExported) {
		this.isExported = isExported;
	}

	public Boolean getIsRequisitionClosed() {
		return isRequisitionClosed;
	}

	public void setIsRequisitionClosed(Boolean isRequisitionClosed) {
		this.isRequisitionClosed = isRequisitionClosed;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	public String getBloodBagIssueNo() {
		return bloodBagIssueNo;
	}

	public void setBloodBagIssueNo(String bloodBagIssueNo) {
		this.bloodBagIssueNo = bloodBagIssueNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
