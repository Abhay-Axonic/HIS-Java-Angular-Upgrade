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
	
	@NamedQuery(name="GET_BEDSIDE_PROCEDURE_MASTER_LIST",
		query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId,"
					+ " bedsideProcedureMaster.bedsideProcedureCode AS bedsideProcedureCode, "
					+ " bedsideProcedureMaster.duration AS duration,"
					+ " bedsideProcedureMaster.isAnaesthesia AS isAnaesthesia, "
					+ " bedsideProcedureMaster.bedsideProcedureDesc AS bedsideProcedureDesc,"
					+ " bedsideProcedureMaster.organizationId AS organizationId, "
					+ " bedsideProcedureMaster.unitId AS unitId, bedsideProcedureMaster.status AS status"
					+ " FROM BedsideProcedureMaster bedsideProcedureMaster"
					+ " WHERE  bedsideProcedureMaster.organizationId= :organizationId "
					+ " AND bedsideProcedureMaster.unitId= :unitId"
					+ " ORDER BY bedsideProcedureMaster.bedsideProcedureId asc"
					),
	
	@NamedQuery(name="GET_ACTIVE_BEDSIDE_PROCEDURE_MASTER",
		query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId, "
				 	+ " bedsideProcedureMaster.bedsideProcedureCode AS bedsideProcedureCode, "
				 	+ " bedsideProcedureMaster.bedsideProcedureDesc AS bedsideProcedureDesc,"
					+ " bedsideProcedureMaster.isAnaesthesia AS isAnaesthesia, "
					+ " bedsideProcedureMaster.duration AS duration,"
					+ " bedsideProcedureMaster.organizationId AS organizationId, "
					+ " bedsideProcedureMaster.unitId AS unitId, "
					+ " bedsideProcedureMaster.status AS status"
					+ " FROM BedsideProcedureMaster bedsideProcedureMaster"
					+ " WHERE bedsideProcedureMaster.status= 'A'"
					+ " AND bedsideProcedureMaster.organizationId= :organizationId"
					+ " AND bedsideProcedureMaster.unitId= :unitId"
					+ " ORDER BY bedsideProcedureMaster.bedsideProcedureId asc"
					),
	
	@NamedQuery(name="GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME",
		query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId "
					+ "	FROM BedsideProcedureMaster bedsideProcedureMaster"
					+ " WHERE LOWER(bedsideProcedureMaster.bedsideProcedureDesc)= :bedsideProcedureDesc"
					),
	
	@NamedQuery(name="GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME_NOT_ID",
		query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId"
					+ " FROM BedsideProcedureMaster bedsideProcedureMaster"
					+ " WHERE LOWER(bedsideProcedureMaster.bedsideProcedureDesc)= :bedsideProcedureDesc"
					+ " AND bedsideProcedureMaster.bedsideProcedureId != :bedsideProcedureId"
					),
	})

@Entity
@Table(name = "m_bedside_procedure_master", schema = "public")
@SequenceGenerator(name = "m_bedside_procedure_master_seq", sequenceName = "public.m_bedside_procedure_master_seq", allocationSize = 1)
public class BedsideProcedureMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_bedside_procedure_master_seq")
	
	@Column(name = "bedside_procedure_id")
	private Integer bedsideProcedureId;
	
	@Column(name = "bedside_procedure_code")
	private String bedsideProcedureCode;
	
	@Column(name = "bedside_procedure_desc")
	private String bedsideProcedureDesc;
	
	@Column(name = "is_anesthesia")
	private Character isAnaesthesia;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "duration")
	private Integer duration;

	public Integer getBedsideProcedureId() {
		return bedsideProcedureId;
	}

	public void setBedsideProcedureId(Integer bedsideProcedureId) {
		this.bedsideProcedureId = bedsideProcedureId;
	}

	public String getBedsideProcedureCode() {
		return bedsideProcedureCode;
	}

	public void setBedsideProcedureCode(String bedsideProcedureCode) {
		this.bedsideProcedureCode = bedsideProcedureCode;
	}

	public String getBedsideProcedureDesc() {
		return bedsideProcedureDesc;
	}

	public void setBedsideProcedureDesc(String bedsideProcedureDesc) {
		this.bedsideProcedureDesc = bedsideProcedureDesc;
	}

	public Character getIsAnaesthesia() {
		return isAnaesthesia;
	}

	public void setIsAnaesthesia(Character isAnaesthesia) {
		this.isAnaesthesia = isAnaesthesia;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	
}
