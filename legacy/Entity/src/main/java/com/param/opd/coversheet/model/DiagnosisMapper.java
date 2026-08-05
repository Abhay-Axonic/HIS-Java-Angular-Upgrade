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

import com.param.global.model.GenderMaster;
@NamedQueries({
	@NamedQuery(name = "GET_LIST_OF_DIAGNOSIS_MAPPER", 
			query = " SELECT 	distinct(diagnosisMapper.diagnosisId) as diagnosisId, diagnosisMapper.isNotifiable as isNotifiable, "
					+ " diagnosisMapper.isCommunicable as isCommunicable, diagnosisMapper.isMedicoLegal as isMedicoLegal, "
					+ " diagnosisMapper.isNonCommunicable as isNonCommunicable,"
//					+ " diagnosisMapper.genderId as genderId, "
					+ " diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName, "
					+ " diagnosisMapper.status as status "
					+ " FROM 	DiagnosisMapper diagnosisMapper "
					+ " INNER JOIN diagnosisMapper.diagnosisMaster diagnosisMaster"
					
					+ " WHERE 	diagnosisMapper.organizationId = :organizationId "
					+ " AND 	diagnosisMapper.isDeleted = 'N' "
					),
	
	@NamedQuery(name = "GET_DIAGNOSIS_MAPPER_BY_ID", 
	query = " SELECT 	distinct(diagnosisMapper.diagnosisId) as diagnosisId, diagnosisMapper.isNotifiable as isNotifiable, "
			+ " diagnosisMapper.isCommunicable as isCommunicable, diagnosisMapper.isMedicoLegal as isMedicoLegal, "
			+ " diagnosisMapper.isNonCommunicable as isNonCommunicable,"
			+ " diagnosisMapper.genderId as genderId, "
			+ " genderMaster.desc as genderDesc, "
			+ " diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName, "
			+ " diagnosisMapper.status as status "
			+ " FROM 	DiagnosisMapper diagnosisMapper "
			+ " INNER JOIN diagnosisMapper.diagnosisMaster diagnosisMaster"
			+ " INNER JOIN diagnosisMapper.genderMaster genderMaster"
			+ " WHERE 	diagnosisMapper.organizationId = :organizationId "
			+ " AND 	diagnosisMapper.diagnosisId = :diagnosisId "
			+ " AND 	diagnosisMapper.isDeleted = 'N' "
			),
	
	@NamedQuery(name = "GET_GENDER_BY_DIAGNOSIS_ID", 
	query = " SELECT 	"
			+ " diagnosisMapper.genderId as genderId, "
			+ " genderMaster.desc as genderDesc "
			+ " FROM 	DiagnosisMapper diagnosisMapper "
			+ " INNER JOIN diagnosisMapper.genderMaster genderMaster"
			+ " WHERE 	diagnosisMapper.organizationId = :organizationId "
			+ " AND 	diagnosisMapper.diagnosisId = :diagnosisId "
			+ " AND 	diagnosisMapper.status = 'A'"
			+ " AND 	diagnosisMapper.isDeleted = 'N'"
			),
	
	@NamedQuery(name = "GET_DIAGNOSIS_BY_GENDER_ID", 
	query = " SELECT 	"
			+ " distinct (diagnosisMapper.diagnosisId) as diagnosisId,diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName "
//			+ " genderMaster.desc as genderDesc "
			+ " FROM 	DiagnosisMapper diagnosisMapper "
			+ " INNER JOIN diagnosisMapper.diagnosisMaster diagnosisMaster "
//			+ " INNER JOIN diagnosisMapper.genderMaster genderMaster"
			+ " WHERE 	diagnosisMapper.organizationId = :organizationId "
			+ " AND (diagnosisMapper.genderId = :genderId or diagnosisMapper.genderId = 3) "
			+ " and 	(LOWER(diagnosisMaster.diagnosisName) LIKE :diagnosisName  or  LOWER(diagnosisMaster.code) LIKE :diagnosisName) "
			+ "  and 	diagnosisMaster.status = 'A' "
			+ " AND 	diagnosisMapper.isDeleted = 'N' "
			),
	

	@NamedQuery(name = "CHECK_DIAGNOSIS_MAPPER_AVAILABLE", 
	query = " SELECT 	"
			+ " diagnosisMapper.diagnosisId as diagnosisId "
			+ " FROM 	DiagnosisMapper diagnosisMapper "
			+ " WHERE 	diagnosisMapper.organizationId = :organizationId "
			+ " AND 	diagnosisMapper.diagnosisId = :diagnosisId "
//			+ " AND 	diagnosisMapper.genderId = :genderId "
			+ " AND 	diagnosisMapper.isDeleted = 'N' "
			),
	
	@NamedQuery(name = "UPDATE_DIAGNOSIS_MAPPER_STATUS",
	query = " UPDATE DiagnosisMapper diagnosisMapper "
			+ " SET diagnosisMapper.status = :status"
			+ " WHERE diagnosisMapper.organizationId= :orgId "
//			+ " AND diagnosisMapper.unitId= :orgUnitId "
			+ " AND diagnosisMapper.diagnosisId = :diagnosisId"),
				})
@Entity 
@Table(name = "t_diagnosis_mapper", schema = "emr")
@SequenceGenerator(name = "t_diagnosis_mapper_seq", sequenceName = "emr.t_diagnosis_mapper_seq", allocationSize = 1)

public class DiagnosisMapper {
	@Id
	@Column(name = "diagnosis_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_diagnosis_mapper_seq")
	private Integer diagnosisMapperId;

	@Column(name = "diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name = "is_notifiable")
	private Character isNotifiable;
	
	@Column(name = "is_communicable")
	private Character isCommunicable;
	
	@Column(name = "is_non_communicable")
	private Character isNonCommunicable;
	
	@Column(name = "is_medico_legal")
	private Character isMedicoLegal;

	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="diagnosis_id", insertable=false, updatable=false)
	private DiagnosisMaster diagnosisMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="gender_id", insertable=false, updatable=false)
	private GenderMaster genderMaster;
	
	public Integer getDiagnosisMapperId() {
		return diagnosisMapperId;
	}

	public void setDiagnosisMapperId(Integer diagnosisMapperId) {
		this.diagnosisMapperId = diagnosisMapperId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Character getIsNotifiable() {
		return isNotifiable;
	}

	public void setIsNotifiable(Character isNotifiable) {
		this.isNotifiable = isNotifiable;
	}

	public Character getIsCommunicable() {
		return isCommunicable;
	}

	public void setIsCommunicable(Character isCommunicable) {
		this.isCommunicable = isCommunicable;
	}

	public Character getIsNonCommunicable() {
		return isNonCommunicable;
	}

	public void setIsNonCommunicable(Character isNonCommunicable) {
		this.isNonCommunicable = isNonCommunicable;
	}

	public Character getIsMedicoLegal() {
		return isMedicoLegal;
	}

	public void setIsMedicoLegal(Character isMedicoLegal) {
		this.isMedicoLegal = isMedicoLegal;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}


	
}
