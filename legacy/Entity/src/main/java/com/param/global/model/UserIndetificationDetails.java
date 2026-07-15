package com.param.global.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({

	@NamedNativeQuery(name="GET_LIST_OF_DOCTOR_IDENTIFICATION_DOCCUEMENT",
	 
	query=" select   " + 
			"    tmp.user_indetification_details_id as \"userIndetificationDetailsId\",   " + 
			"	tmp.identification_type_id as \"identificationTypeId\",   " + 
			"	tmp.identification_number as \"identificationNumber\",   " + 
			"   to_char(tmp.identification_expiry_date,'DD/MM/YYYY') as \"identificationExpiryDate\",  " +
			"	tmp.doctor_indetification_file as \"doctorIndetificationFile\",   " + 
			"	tmp.file_name as \"fileName\"   " + 
			"from   " + 
			"	public.t_user_indetification_details tmp   " + 
			"where   " + 
			"	tmp.status = 'A'   " + 
			"	and tmp.doctor_id =:doctorId   " + 
			"	and tmp.organization_id =:organizationId   " + 
			"	and tmp.unit_id =:unitId " ),
	
	
	
	@NamedNativeQuery(name="GET_LIST_OF_STAFF_IDENTIFICATION_DOCCUEMENT",
	
	query=" select   " + 
			"    tmp.user_indetification_details_id as \"userIndetificationDetailsId\",   " + 
			"	tmp.identification_type_id as \"identificationTypeId\",   " + 
			"	tmp.identification_number as \"identificationNumber\",   " + 
			"   to_char(tmp.identification_expiry_date,'DD/MM/YYYY') as \"identificationExpiryDate\",  " +
			"	tmp.doctor_indetification_file as \"doctorIndetificationFile\",   " + 
			"	tmp.file_name as \"fileName\"   " + 
			"from   " + 
			"	public.t_user_indetification_details tmp   " + 
			"where   " + 
			"	tmp.status = 'A'   " + 
			"	and tmp.employee_id =:employeeId   " + 
			"	and tmp.organization_id =:organizationId   " + 
			"	and tmp.unit_id =:unitId " ),

})

@Entity
@Table(name="t_user_indetification_details",schema="public")
@SequenceGenerator(name="t_user_indetification_details_seq",sequenceName="public.t_user_indetification_details_seq",allocationSize=1)

public class UserIndetificationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_user_indetification_details_seq")
	@Column(name = "user_indetification_details_id")
	private Integer userIndetificationDetailsId;
	
	@Column(name="identification_type_id")
	private Integer identificationTypeId;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="identification_expiry_date")
	private Date identificationExpiryDate;
	
	@Column(name="doctor_indetification_file")
	private String doctorIndetificationFile;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "employee_id")
	private Integer employeeId;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getUserIndetificationDetailsId() {
		return userIndetificationDetailsId;
	}

	public void setUserIndetificationDetailsId(Integer userIndetificationDetailsId) {
		this.userIndetificationDetailsId = userIndetificationDetailsId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Date getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(Date identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public String getDoctorIndetificationFile() {
		return doctorIndetificationFile;
	}

	public void setDoctorIndetificationFile(String doctorIndetificationFile) {
		this.doctorIndetificationFile = doctorIndetificationFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
		
}
