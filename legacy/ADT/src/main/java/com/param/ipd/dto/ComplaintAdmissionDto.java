package com.param.ipd.dto;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;



public class ComplaintAdmissionDto {

	
	private Integer complaintAdmissionId;

	private Integer admissionId;

	private Character status;

	private Integer createdBy;

	private Integer updatedBy;

	private String createdDate;
	private String createdDate1;
	
	private String ceratedDate;
	private Date createdDateNew;
	
	private String updatedDate;

	private Integer unitId;

	private Integer organizationId;

	private Integer diagnosisId;

	private String remark;

	private Integer roleId;

	private Character isReviewedFlag;

	private String complaintsDateTime;
	
	private String complaintsDate;
	
	private String complaintsTime;
	
	private String diagnosisName;
	
	private int complaintId;
	
	private Integer icpcSystemId;
	
	private String icpcSystemDesc;
	
	private Integer since;
	
	private String duration;
	
	private String icpcCode;
	
	private String icpcCodeText;
	
	private String justificationComplaint;
	
	private Character isComplaintStop;
	
	private Long complaintTempCount;
	
	private Integer patientId;
	private Integer mIcpcId1;
	private BigInteger mIcpcId;
	private String icpcDesc; 
	private String employeeName; 
	private String doctorName; 
	private Integer visitTypeId;
	private Integer encounterId;
	private Integer complaintAppoId;
	private String resolvedRemark;
	
	private Character isResolved;
	
	private String resolvedDate;
	
	private List<ComplaintAdmissionDetailsDto> listComplaintAdmissionDetailsDto;
	private Integer templateIcpcId;
	private String userName;

	private List<ComplaintAdmissionDto> activeCompliantList;

	private String addedDate;

	private List<String> dates;
	private String createdDate2;

	private String parseCasesheetHtml;

	private Integer doctorId;
	private Integer specialityId;

	private String uuid;

	private String documentPath;

	public String getCreatedDate2() {
		return createdDate2;
	}

	public void setCreatedDate2(String createdDate2) {
		this.createdDate2 = createdDate2;
	}

	public Integer getComplaintAdmissionId() {
		return complaintAdmissionId;
	}

	public void setComplaintAdmissionId(Integer complaintAdmissionId) {
		this.complaintAdmissionId = complaintAdmissionId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Character getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(Character isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public String getComplaintsDateTime() {
		return complaintsDateTime;
	}

	public void setComplaintsDateTime(String complaintsDateTime) {
		this.complaintsDateTime = complaintsDateTime;
	}

	public List<ComplaintAdmissionDetailsDto> getListComplaintAdmissionDetailsDto() {
		return listComplaintAdmissionDetailsDto;
	}

	public void setListComplaintAdmissionDetailsDto(List<ComplaintAdmissionDetailsDto> listComplaintAdmissionDetailsDto) {
		this.listComplaintAdmissionDetailsDto = listComplaintAdmissionDetailsDto;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	public String getComplaintsDate() {
		return complaintsDate;
	}

	public void setComplaintsDate(String complaintsDate) {
		this.complaintsDate = complaintsDate;
	}

	public String getComplaintsTime() {
		return complaintsTime;
	}

	public void setComplaintsTime(String complaintsTime) {
		this.complaintsTime = complaintsTime;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public Integer getSince() {
		return since;
	}

	public void setSince(Integer since) {
		this.since = since;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCeratedDate() {
		return ceratedDate;
	}

	public void setCeratedDate(String ceratedDate) {
		this.ceratedDate = ceratedDate;
	}

	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}

	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
	}

	public String getIcpcCode() {
		return icpcCode;
	}

	public void setIcpcCode(String icpcCode) {
		this.icpcCode = icpcCode;
	}

	public String getIcpcCodeText() {
		return icpcCodeText;
	}

	public void setIcpcCodeText(String icpcCodeText) {
		this.icpcCodeText = icpcCodeText;
	}

	public Integer getTemplateIcpcId() {
		return templateIcpcId;
	}

	public void setTemplateIcpcId(Integer templateIcpcId) {
		this.templateIcpcId = templateIcpcId;
	}

	public Long getComplaintTempCount() {
		return complaintTempCount;
	}

	public void setComplaintTempCount(Long complaintTempCount) {
		this.complaintTempCount = complaintTempCount;
	}

	public String getJustificationComplaint() {
		return justificationComplaint;
	}

	public void setJustificationComplaint(String justificationComplaint) {
		this.justificationComplaint = justificationComplaint;
	}

	public Character getIsComplaintStop() {
		return isComplaintStop;
	}

	public void setIsComplaintStop(Character isComplaintStop) {
		this.isComplaintStop = isComplaintStop;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getComplaintAppoId() {
		return complaintAppoId;
	}

	public void setComplaintAppoId(Integer complaintAppoId) {
		this.complaintAppoId = complaintAppoId;
	}

	public Integer getmIcpcId1() {
		return mIcpcId1;
	}

	public void setmIcpcId1(Integer mIcpcId1) {
		this.mIcpcId1 = mIcpcId1;
	}

	
	public String getIcpcDesc() {
		return icpcDesc;
	}

	public void setIcpcDesc(String icpcDesc) {
		this.icpcDesc = icpcDesc;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public Character getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Character isResolved) {
		this.isResolved = isResolved;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public BigInteger getmIcpcId() {
		return mIcpcId;
	}

	public void setmIcpcId(BigInteger mIcpcId) {
		this.mIcpcId = mIcpcId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}

	public List<ComplaintAdmissionDto> getActiveCompliantList() {
		return activeCompliantList;
	}

	public void setActiveCompliantList(List<ComplaintAdmissionDto> activeCompliantList) {
		this.activeCompliantList = activeCompliantList;
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

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}
	
	

	public String getParseCasesheetHtml() {
		return parseCasesheetHtml;
	}

	public void setParseCasesheetHtml(String parseCasesheetHtml) {
		this.parseCasesheetHtml = parseCasesheetHtml;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
}
