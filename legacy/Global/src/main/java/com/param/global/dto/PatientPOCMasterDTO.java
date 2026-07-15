package com.param.global.dto;

import javax.persistence.Column;
import java.util.Date;

public class PatientPOCMasterDTO {

	private Integer patientPOCId;

	private String patientPOCUniqueNo;

	private String firstName;

	private String middleName;

	private String lastName;

	private String emailId;

	private Integer prefixId;

	private String mobileNo;

	private Integer reasonId;

	private String remark;

	private Integer checkinStatusId;

	private char status;
	
	private Integer genderId;

	private String checkinDateTime;

	private Date patientPreDateTime;

	private Date wheelInDateTime;

	private Date wheelOutDateTime;

	private Date patientExitDateTime;

	private String createdDate;

	private String updatedDate;

	private Integer organizationId;

	private Integer unitId;
	
	private Integer registrationTypeId;

	public Integer getPatientPOCId() {
		return patientPOCId;
	}

	public void setPatientPOCId(Integer patientPOCId) {
		this.patientPOCId = patientPOCId;
	}

	public String getPatientPOCUniqueNo() {
		return patientPOCUniqueNo;
	}

	public void setPatientPOCUniqueNo(String patientPOCUniqueNo) {
		this.patientPOCUniqueNo = patientPOCUniqueNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCheckinStatusId() {
		return checkinStatusId;
	}

	public void setCheckinStatusId(Integer checkinStatusId) {
		this.checkinStatusId = checkinStatusId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
	
	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getCheckinDateTime() {
		return checkinDateTime;
	}

	public void setCheckinDateTime(String checkinDateTime) {
		this.checkinDateTime = checkinDateTime;
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

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public Date getPatientPreDateTime() {
		return patientPreDateTime;
	}

	public void setPatientPreDateTime(Date patientPreDateTime) {
		this.patientPreDateTime = patientPreDateTime;
	}

	public Date getWheelInDateTime() {
		return wheelInDateTime;
	}

	public void setWheelInDateTime(Date wheelInDateTime) {
		this.wheelInDateTime = wheelInDateTime;
	}

	public Date getWheelOutDateTime() {
		return wheelOutDateTime;
	}

	public void setWheelOutDateTime(Date wheelOutDateTime) {
		this.wheelOutDateTime = wheelOutDateTime;
	}

	public Date getPatientExitDateTime() {
		return patientExitDateTime;
	}

	public void setPatientExitDateTime(Date patientExitDateTime) {
		this.patientExitDateTime = patientExitDateTime;
	}
}
