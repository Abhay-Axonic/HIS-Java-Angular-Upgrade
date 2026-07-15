package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_DOCTOR_FAVOURITES_DIAGNOSIS",
			query="SELECT docFavDig.docFavDiagId as docFavDiagId, "
			+ "docFavDig.doctorId as doctorId, "
			+ "docFavDig.diagnosisId as diagnosisId "
			+ "FROM DoctorFavouritesDiagnosis docFavDig "
            + "WHERE docFavDig.doctorId=:doctorId "
            + "AND docFavDig.diagnosisId=:diagnosisId "
            + "AND docFavDig.organizationId=:orgId "
            + "AND docFavDig.unitId=:unitId "
            + "AND docFavDig.status='A' "),
	
			@NamedQuery(name="GET_DOCTOR_FAVOURITE_DIAGNOSIS_LIST_BY_DOC_ID",
			query="SELECT docFavDig.docFavDiagId as docFavDiagId, "
			+ " docFavDig.doctorId as doctorId, "
			+ " docFavDig.diagnosisId as diagnosisId ,"
			+ " diagnosisMaster.diagnosisDesc as diagnosisDesc "
			+ " FROM DoctorFavouritesDiagnosis docFavDig "
			+ " INNER JOIN docFavDig.diagnosisMaster diagnosisMaster "
		    + " WHERE docFavDig.organizationId=:orgId "
		    + " AND docFavDig.unitId=:unitId "
		    + " AND docFavDig.status='A' "),
	
})

@Entity 
@Table(name = "t_doctor_favourites_diagnosis", schema = "patient")
@SequenceGenerator(name = "t_doctor_favourites_diagnosis_seq", sequenceName = "patient.t_doctor_favourites_diagnosis_seq", allocationSize = 1)
public class DoctorFavouritesDiagnosis {
	@Id
	@Column(name = "doc_fav_diag_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_favourites_diagnosis_seq")
	private Integer docFavDiagId;

	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosis_id", insertable = false, nullable = false, updatable = false)
	private DiagnosisMaster diagnosisMaster;

	public Integer getDocFavDiagId() {
		return docFavDiagId;
	}

	public void setDocFavDiagId(Integer docFavDiagId) {
		this.docFavDiagId = docFavDiagId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
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
