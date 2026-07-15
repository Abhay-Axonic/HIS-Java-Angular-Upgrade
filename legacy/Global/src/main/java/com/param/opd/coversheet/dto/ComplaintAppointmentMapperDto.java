package com.param.opd.coversheet.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class ComplaintAppointmentMapperDto {

	private int complaintId;
	private Integer encounterId;
	private Integer unitId;
	private Integer organizationId;
	private char status;
	private Integer createdBy;
	private String ceratedDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer roleId;
	private Integer complaintAppoId;
	private char isReviewedFlag;
	private Integer icpcSystemId;
	private List<ComplaintAppointmentDetailsDto> listComplaintAppointmentDetailsDto;
	private Integer diagnosisId;
	private String diagnosisName;
	private String duration;
	private String remark;
	private Integer since;
	private Integer patientId;
	private Integer admissionId;
	private Integer appointmentId;
	private Integer templateIcpcId;
	private String icpcSystemDesc;
	private String icpcCode;
	private String fromDate;
	private String toDate;
	private String employeeName;
	private String doctorName;
	private boolean editComplaint;
	private String firstName;
	private Integer mIcpcId;
	private Integer mIcpcId1;
	private Integer covTemplateId;

	private String justificationComplaint;
	
	private char isComplaintStop;
	private Integer complaintAdmissionId;
	private String resolvedRemark;
	private Integer visitTypeId;

	private Character isResolved;
	
	private String resolvedDate;

	private String createdDate;
	

	private String complaintsDateTime;
	
	private String complaintsDate;
	
	private String complaintsTime;

	private String systemName;
	private String icpcCoddeText;
	private Long complaintTempCount;
	private BigInteger complaintTempCountBook;
	private BigInteger count;
	private String lastName;
	private String userName;
	private String searchIcpcCodeText;
	
	private String icpcCodeText;
	
	private Integer icpcId;
	
	private String icpcDesc;
	
	private String icpcSystemCode;
	private String ceratedDateNew;

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}
	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}
	public int getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCeratedDate() {
		return ceratedDate;
	}
	public void setCeratedDate(String ceratedDate) {
		this.ceratedDate = ceratedDate;
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
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getComplaintAppoId() {
		return complaintAppoId;
	}
	public void setComplaintAppoId(Integer complaintAppoId) {
		this.complaintAppoId = complaintAppoId;
	}
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public List<ComplaintAppointmentDetailsDto> getListComplaintAppointmentDetailsDto() {
		return listComplaintAppointmentDetailsDto;
	}
	public void setListComplaintAppointmentDetailsDto(
			List<ComplaintAppointmentDetailsDto> listComplaintAppointmentDetailsDto) {
		this.listComplaintAppointmentDetailsDto = listComplaintAppointmentDetailsDto;
	}
	public Integer getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSince() {
		return since;
	}
	public void setSince(Integer since) {
		this.since = since;
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
	public String getJustificationComplaint() {
		return justificationComplaint;
	}
	public void setJustificationComplaint(String justificationComplaint) {
		this.justificationComplaint = justificationComplaint;
	}
	public char getIsComplaintStop() {
		return isComplaintStop;
	}
	public void setIsComplaintStop(char isComplaintStop) {
		this.isComplaintStop = isComplaintStop;
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
	public Integer getComplaintAdmissionId() {
		return complaintAdmissionId;
	}
	public void setComplaintAdmissionId(Integer complaintAdmissionId) {
		this.complaintAdmissionId = complaintAdmissionId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getComplaintsDateTime() {
		return complaintsDateTime;
	}
	public void setComplaintsDateTime(String complaintsDateTime) {
		this.complaintsDateTime = complaintsDateTime;
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
	public Long getComplaintTempCount() {
		return complaintTempCount;
	}
	public void setComplaintTempCount(Long complaintTempCount) {
		this.complaintTempCount = complaintTempCount;
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
	public BigInteger getComplaintTempCountBook() {
		return complaintTempCountBook;
	}
	public void setComplaintTempCountBook(BigInteger complaintTempCountBook) {
		this.complaintTempCountBook = complaintTempCountBook;
	}
	public Integer getIcpcId() {
		return icpcId;
	}
	public void setIcpcId(Integer icpcId) {
		this.icpcId = icpcId;
	}
	public String getIcpcDesc() {
		return icpcDesc;
	}
	public void setIcpcDesc(String icpcDesc) {
		this.icpcDesc = icpcDesc;
	}
	public String getIcpcSystemCode() {
		return icpcSystemCode;
	}
	public void setIcpcSystemCode(String icpcSystemCode) {
		this.icpcSystemCode = icpcSystemCode;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getIcpcCoddeText() {
		return icpcCoddeText;
	}
	public void setIcpcCoddeText(String icpcCoddeText) {
		this.icpcCoddeText = icpcCoddeText;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getTemplateIcpcId() {
		return templateIcpcId;
	}
	public void setTemplateIcpcId(Integer templateIcpcId) {
		this.templateIcpcId = templateIcpcId;
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
	public boolean isEditComplaint() {
		return editComplaint;
	}
	public void setEditComplaint(boolean editComplaint) {
		this.editComplaint = editComplaint;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Integer getmIcpcId() {
		return mIcpcId;
	}
	public void setmIcpcId(Integer mIcpcId) {
		this.mIcpcId = mIcpcId;
	}
	public Integer getmIcpcId1() {
		return mIcpcId1;
	}
	public void setmIcpcId1(Integer mIcpcId1) {
		this.mIcpcId1 = mIcpcId1;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getSearchIcpcCodeText() {
		return searchIcpcCodeText;
	}
	public void setSearchIcpcCodeText(String searchIcpcCodeText) {
		this.searchIcpcCodeText = searchIcpcCodeText;
	}
	public String getCeratedDateNew() {
		return ceratedDateNew;
	}
	public void setCeratedDateNew(String ceratedDateNew) {
		this.ceratedDateNew = ceratedDateNew;
	}
	public Integer getCovTemplateId() {
		return covTemplateId;
	}
	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
		
}
