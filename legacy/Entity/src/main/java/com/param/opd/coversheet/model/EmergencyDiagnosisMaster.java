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
	
	@NamedQuery(name = "GET_LIST_OF_EMERGENCY_DIAGNOSIS_MASTER", 
	query = " SELECT emergencyDiagnosisMaster.diagnosisId as diagnosisId,"
			+ " emergencyDiagnosisMaster.emergencyDiagnosisMapperId as emergencyDiagnosisMapperId, "
			+ " diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName, "
			+ " emergencyDiagnosisMaster.status as status "
			+ " FROM 	EmergencyDiagnosisMaster emergencyDiagnosisMaster "
			+ " INNER JOIN emergencyDiagnosisMaster.diagnosisMaster diagnosisMaster"
			+ " WHERE 	emergencyDiagnosisMaster.organizationId = :organizationId "
			),
	@NamedQuery(name = "GET_EMERGENCY_DIAGNOSIS_MASTER_LIST_BY_ID", 
	query = " SELECT emergencyDiagnosisMaster.diagnosisId as diagnosisId,"
			+ " emergencyDiagnosisMaster.emergencyDiagnosisMapperId as emergencyDiagnosisMapperId, "
			+ " diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName, "
			+ " emergencyDiagnosisMaster.status as status "
			+ " FROM 	EmergencyDiagnosisMaster emergencyDiagnosisMaster "
			+ " INNER JOIN emergencyDiagnosisMaster.diagnosisMaster diagnosisMaster"
			+ " WHERE emergencyDiagnosisMaster.emergencyDiagnosisMapperId =:emergencyDiagnosisMapperId "
			+ "	AND emergencyDiagnosisMaster.organizationId = :organizationId "
			),
	@NamedQuery(name = "GET_EMERGENCY_DIAGNOSIS_MASTER_BY_ID", 
	query = " SELECT emergencyDiagnosisMaster.diagnosisId as diagnosisId "
			+ " FROM 	EmergencyDiagnosisMaster emergencyDiagnosisMaster "
			+ " WHERE 	emergencyDiagnosisMaster.diagnosisId = :diagnosisId "
			+ "and  emergencyDiagnosisMaster.organizationId = :organizationId "
			),
	
	@NamedQuery(name = "GET_ACTIVE_EMERGENCY_DIAGNOSIS_MASTER_LIST", 
	query =  " SELECT emergencyDiagnosisMaster.diagnosisId as diagnosisId,"
			+ " emergencyDiagnosisMaster.emergencyDiagnosisMapperId as emergencyDiagnosisMapperId, "
			+ " diagnosisMaster.code as code, diagnosisMaster.diagnosisName as diagnosisName, "
			+ " emergencyDiagnosisMaster.status as status "
			+ " FROM 	EmergencyDiagnosisMaster emergencyDiagnosisMaster "
			+ " INNER JOIN emergencyDiagnosisMaster.diagnosisMaster diagnosisMaster"
			+ " WHERE 	emergencyDiagnosisMaster.organizationId = :organizationId "
			+ " and (LOWER(diagnosisMaster.diagnosisName) LIKE :diagnosisName  or  LOWER(diagnosisMaster.code) LIKE :diagnosisName) "
			+ "AND emergencyDiagnosisMaster.status = 'A'")
			
		
})

@Entity 
@Table(name = "t_emergency_diagnosis_mapper", schema = "emr")
@SequenceGenerator(name = "t_emergency_diagnosis_mapper_seq", sequenceName = "emr.t_emergency_diagnosis_mapper_seq", allocationSize = 1)

public class EmergencyDiagnosisMaster {
	@Id
	@Column(name = "emergency_diagnosis_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_emergency_diagnosis_mapper_seq")
	private Integer emergencyDiagnosisMapperId;

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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="diagnosis_id", insertable=false, updatable=false)
	private DiagnosisMaster diagnosisMaster;
	
	
	public Integer getEmergencyDiagnosisMapperId() {
		return emergencyDiagnosisMapperId;
	}

	public void setEmergencyDiagnosisMapperId(Integer emergencyDiagnosisMapperId) {
		this.emergencyDiagnosisMapperId = emergencyDiagnosisMapperId;
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
