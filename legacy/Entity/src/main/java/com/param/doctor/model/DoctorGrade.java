package com.param.doctor.model;

import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.otims.model.SurgeryResourceConfig;
@NamedQueries({
				@NamedQuery(name="GET_DOCTOR_GRADE_LIST",
						query=" SELECT "
								+ " doctorGrade.doctorGradeId AS doctorGradeId, doctorGrade.docGradeCode AS docGradeCode, "
								+ " doctorGrade.docGradeName AS docGradeName, doctorGrade.status AS status, "
								+ " doctorGrade.orgId AS orgId, doctorGrade.orgUnitId AS orgUnitId"
								+ " FROM DoctorGrade doctorGrade WHERE doctorGrade.orgId= :orgId AND doctorGrade.orgUnitId= :orgUnitId ORDER BY doctorGrade.doctorGradeId asc"
								),
				
				@NamedQuery(name="GET_DOCTOR_GRADE_BY_ID",
						query=" SELECT "
								+ " doctorGrade.doctorGradeId AS doctorGradeId, doctorGrade.docGradeCode AS docGradeCode, "
								+ " doctorGrade.docGradeName AS docGradeName, doctorGrade.status AS status, "
								+ " doctorGrade.orgId AS orgId, doctorGrade.orgUnitId AS orgUnitId"
								+ " FROM DoctorGrade doctorGrade WHERE doctorGrade.doctorGradeId= :doctorGradeId AND doctorGrade.orgId= :orgId AND doctorGrade.orgUnitId= :orgUnitId"
								+ " ORDER BY doctorGrade.doctorGradeId asc"
								),
				
				@NamedQuery(name="GET_ACTIVE_DOCTOR_GRADE_LIST",
						query=" SELECT "
								+ " doctorGrade.doctorGradeId AS doctorGradeId, doctorGrade.docGradeCode AS docGradeCode, "
								+ " doctorGrade.docGradeName AS docGradeName, doctorGrade.status AS status, "
								+ " doctorGrade.orgId AS orgId, doctorGrade.orgUnitId AS orgUnitId"
								+ " FROM DoctorGrade doctorGrade WHERE doctorGrade.orgId= :orgId AND doctorGrade.orgUnitId= :orgUnitId AND doctorGrade.status= 'A'"
								+ " ORDER BY doctorGrade.doctorGradeId asc"
								),
						
				@NamedQuery(name="GET_DOCTOR_GRADE_BY_NAME",
						query="SELECT "
								+ " doctorGrade.doctorGradeId AS doctorGradeId, doctorGrade.docGradeName AS docGradeName "
								+ " FROM DoctorGrade doctorGrade WHERE LOWER(doctorGrade.docGradeName)= :docGradeName "
								),
				
				
				@NamedQuery(name="GET_DOCTOR_GRADE_BY_NAME_NOT_ID",
						query="SELECT "
								+ " doctorGrade.doctorGradeId AS doctorGradeId, doctorGrade.docGradeName AS docGradeName "
								+ " FROM DoctorGrade doctorGrade WHERE LOWER(doctorGrade.docGradeName)= :docGradeName AND doctorGrade.doctorGradeId != :doctorGradeId"
								)
})
@Entity
@Table(name = "m_doctor_grade" , schema = "doctor")
@SequenceGenerator(name = "m_seq_doc_grade" , sequenceName = "doctor.m_seq_doc_grade" , allocationSize = 1)
public class DoctorGrade {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_seq_doc_grade")
	@Column(name = "doctor_grade_id")
	private int doctorGradeId;
	
	@Column(name = "doc_grade_code")
	private String docGradeCode;
	
	@Column(name = "doc_grade_name")
	private String docGradeName;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "doctorGrade")
	private List<SurgeryResourceConfig> listSurgeryResourceConfig;

	public List<SurgeryResourceConfig> getListSurgeryResourceConfig() {
		return listSurgeryResourceConfig;
	}

	public void setListSurgeryResourceConfig(List<SurgeryResourceConfig> listSurgeryResourceConfig) {
		this.listSurgeryResourceConfig = listSurgeryResourceConfig;
	}

	public int getDoctorGradeId() {
		return doctorGradeId;
	}

	public void setDoctorGradeId(int doctorGradeId) {
		this.doctorGradeId = doctorGradeId;
	}

	public String getDocGradeCode() {
		return docGradeCode;
	}

	public void setDocGradeCode(String docGradeCode) {
		this.docGradeCode = docGradeCode;
	}

	public String getDocGradeName() {
		return docGradeName;
	}

	public void setDocGradeName(String docGradeName) {
		this.docGradeName = docGradeName;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
}
