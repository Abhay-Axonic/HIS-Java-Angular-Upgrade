package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name = "GET_POC_MASTER_BY_POC_UNIQUE_NUMBER", 
			query = "SELECT patientPOCMaster.patientPOCId as patientPOCId, patientPOCMaster.patientPOCUniqueNo as patientPOCUniqueNo, patientPOCMaster.firstName as firstName, patientPOCMaster.middleName as middleName, patientPOCMaster.lastName as lastName, patientPOCMaster.emailId as emailId, patientPOCMaster.prefixId as prefixId, patientPOCMaster.mobileNo as mobileNo, patientPOCMaster.reasonId as reasonId,"						
					+" patientPOCMaster.remark as remark, patientPOCMaster.checkinStatusId as checkinStatusId, patientPOCMaster.status as status, patientPOCMaster.genderId as genderId, to_char(patientPOCMaster.checkinDateTime, 'DD-MM-YYYY') as checkinDateTime, to_char(patientPOCMaster.createdDate, 'DD-MM-YYYY') as createdDate, to_char(patientPOCMaster.updatedDate, 'DD-MM-YYYY') as updatedDate"
					+" FROM PatientPOCMaster as patientPOCMaster"
					+" WHERE patientPOCMaster.patientPOCUniqueNo = :patientSearch"
					+" OR patientPOCMaster.mobileNo = :patientSearch"
					),
	
})

@Entity
@Table(name = "m_patient_poc_master", schema = "public")
@SequenceGenerator(name = "seq_m_patient_poc_master", sequenceName = "public.seq_m_patient_poc_master", allocationSize = 1)
public class PatientPOCMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_patient_poc_master")
	@Column(name = "patient_poc_id")
	private Integer patientPOCId;

	@Column(name = "poc_unique_no")
	private String patientPOCUniqueNo;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "prefix_id")
	private Integer prefixId;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "reason_id")
	private Integer reasonId;

	@Column(name = "remark")
	private String remark;

	@Column(name = "checkin_status_id")
	private Integer checkinStatusId;

	@Column(name = "status")
	private char status;
	
	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "checkin_date_time")
	private Date checkinDateTime;

	@Column(name = "patient_pre_date_time")
	private Date patientPreDateTime;

	@Column(name = "wheel_in_date_time")
	private Date wheelInDateTime;

	@Column(name = "wheel_out_date_time")
	private Date wheelOutDateTime;

	@Column(name = "patient_exit_date_time")
	private Date patientExitDateTime;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

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

	public Date getCheckinDateTime() {
		return checkinDateTime;
	}

	public void setCheckinDateTime(Date checkinDateTime) {
		this.checkinDateTime = checkinDateTime;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
