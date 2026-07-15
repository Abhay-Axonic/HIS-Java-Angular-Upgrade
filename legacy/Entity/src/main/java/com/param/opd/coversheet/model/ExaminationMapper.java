package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.NamedNativeQuery;

import com.param.global.model.ExaminationMaster;

@NamedQueries({

	@NamedQuery(name = "GET_EXAMINATION_MAPPER_LIST", 
			query = " SELECT  "
					+ " examn.examinationMapperId as examinationMapperId, "
					+ " examn.examinationMasterId as examinationMasterId, "
					+ " examn.remark as remark, "
//					+ " examinationMaster.examinationCode as examinationCode, "
					+ " examinationMaster.examinationDesc as examinationDesc, "
					+ " examn.status as status "
					+ " FROM ExaminationMapper examn "
					+ " INNER JOIN examn.examinationMaster examinationMaster" 
					+ " WHERE examn.organizationId=:organizationId "
					+ " and examn.unitId=:unitId"
					+ " and examn.patientId=:patientId"
					+ " and examn.status='A'"
					+ " order by examn.examinationMapperId desc"

					),

	@NamedQuery(name = "GET_EXAMINATION_MAPPER_BY_ID", 
	query = " SELECT "
			+ " examn.examinationMapperId as examinationMapperId, "
			+ " examn.examinationMasterId as examinationMasterId, "
			+ " examn.remark as remark, "
			+ " examinationMaster.examinationDesc as examinationDesc, "
			+ " examn.status as status "
			+ " FROM ExaminationMapper examn "
			+ " INNER JOIN examn.examinationMaster examinationMaster" 
			+ " WHERE examn.organizationId=:organizationId "
			+ " and examn.examinationMasterId=:examinationMasterId"
			+ " and examn.unitId=:unitId"
			+ " and examn.status='A'"
			),
	
	@NamedQuery(name = "CHECK_AVAILABLE_EXAMINATION_MAPPER", 
	query = " SELECT "
			+ " examn.examinationMapperId as examinationMapperId, "
			+ " examn.examinationMasterId as examinationMasterId, "
			+ " examinationMaster.examinationDesc as examinationDesc "
			+ " FROM ExaminationMapper examn "
			+ " INNER JOIN examn.examinationMaster examinationMaster" 
			+ " WHERE examn.organizationId=:organizationId "
			+ " and examn.examinationMasterId=:exmnMasterId"
			+ " and examn.unitId=:unitId"
			+ " and examn.status='A'"
			+ " and examn.patientId=:patientId"
			+ " and examn.encounterId=:encounterId"
			),

})

@NamedNativeQuery(name ="GET_MOSTLY_EXAMINATION_MAPPER_LIST_FOR" , 
 query = "select  " + 
 		"	mppr.examination_master_id as \"examinationMasterId\",  " + 
 		"	mem.examination_desc as \"examinationDesc\",  " + 
 		"	count(*)  " + 
 		"from  " + 
 		"	patient.t_examination_mapper mppr  " + 
 		"inner join emr.m_examination_master mem  on  " + 
 		"	mem.examination_master_id = mppr.examination_master_id   " + 
 		"  " + 
 		"where  " + 
 		"	mppr.organization_id = :organizationId  " + 
 		"	and mppr.unit_id = :unitId  " + 
 		"	and mppr.status = 'A'  " + 
 		"group by  " + 
 		"	mppr.examination_master_id,  " + 
 		"	mem.examination_desc  " + 
 		"	  " + 
 		"order by  " + 
 		"	count(*) desc  " + 
 		"limit 10;")

@Entity
@Table(name="t_examination_mapper", schema="patient")
@SequenceGenerator(name = "t_examination_mapper_seq", sequenceName = "patient.t_examination_mapper_seq", allocationSize = 1)
public class ExaminationMapper {
	@Id
	@Column(name="examination_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_examination_mapper_seq")
	private Integer examinationMapperId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="examination_master_id")
	private Integer examinationMasterId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examination_master_id", insertable = false, nullable = false, updatable = false)
	private ExaminationMaster examinationMaster;

	public Integer getExaminationMapperId() {
		return examinationMapperId;
	}

	public void setExaminationMapperId(Integer examinationMapperId) {
		this.examinationMapperId = examinationMapperId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getExaminationMasterId() {
		return examinationMasterId;
	}

	public void setExaminationMasterId(Integer examinationMasterId) {
		this.examinationMasterId = examinationMasterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
}
