package com.param.otims.model;

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

import com.param.global.model.SpecialityMaster;

@NamedQueries({
				@NamedQuery(name="GET_SUBSEQUENT_PROCEDURE_CONFIG_LIST",
						query="SELECT"
								+ " subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
								+ " subSequentProcedure.specialityId AS specialityId,"
								+ " splMstr.specialityName AS specialityName, "
								+ " subSequentProcedure.status AS status, "
								+ " subSequentProcedure.orgId AS orgId, "
								+ " subSequentProcedure.unitId AS unitId "
								+ " FROM SubSequentProcedureConfig subSequentProcedure "
								+ " LEFT JOIN subSequentProcedure.specialityMaster splMstr "
								+ " WHERE subSequentProcedure.unitId=:unitId "
								+ " AND subSequentProcedure.orgId=:orgId "
								+ " order by subSequentProcConfigId desc"
								),
				
				
				@NamedQuery(name="GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ID", query="SELECT"
								+ " subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
								+ " subSequentProcedure.specialityId AS specialityId, "
								+ " subSequentProcedure.status AS status, "
								+ " subSequentProcedure.orgId AS orgId, "
								+ " subSequentProcedure.unitId AS unitId "
								+ " FROM SubSequentProcedureConfig subSequentProcedure"
								+ " WHERE subSequentProcedure.subSequentProcConfigId = :subSequentProcConfigId"
								+ " AND subSequentProcedure.orgId = :orgId"
								+ " AND subSequentProcedure.unitId = :unitId"
								+ " order by subSequentProcConfigId asc"
								),
				
				
				@NamedQuery(name="GET_ACTIVE_SUBSEQUENT_PROCEDURE_CONFIG_LIST", query="SELECT"
								+ " subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
								+ " subSequentProcedure.status AS status, "
								+ " subSequentProcedure.orgId AS orgId "
								+ " FROM SubSequentProcedureConfig subSequentProcedure"
								+ " WHERE subSequentProcedure.status = 'A'"
								+ " AND subSequentProcedure.orgId = :orgId"
								+ " order by subSequentProcConfigId asc"
								
								),
				
				
				@NamedQuery(name="GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ID_NOT_NAME", query="SELECT"
								+ " subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
								+ " subSequentProcedure.specialityId AS specialityId "
								+ " FROM SubSequentProcedureConfig subSequentProcedure"
								+ " WHERE subSequentProcedure.specialityId = :specialityId"
								+ " AND subSequentProcedure.orgId = :orgId "
								+ " AND subSequentProcedure.unitId = :unitId "
								),
				
				@NamedQuery(name="GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ORG_UNIT_STATUS", 
				        query="SELECT subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
						+ " subSequentProcedure.specialityId AS specialityId, subSequentProcedure.status AS status, subSequentProcedure.orgId AS orgId, "
						+ " subSequentProcedure.unitId AS unitId "
						+ " FROM SubSequentProcedureConfig subSequentProcedure"
						+ " WHERE subSequentProcedure.orgId = :orgId "
						+ " AND subSequentProcedure.unitId = :unitId "
						+ " AND subSequentProcedure.status = :status "),
				
				@NamedQuery(name="GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_PRIMID", query="SELECT"
						+ " subSequentProcedure.subSequentProcConfigId AS subSequentProcConfigId, "
						+ " subSequentProcedure.specialityId AS specialityId "
						+ " FROM SubSequentProcedureConfig subSequentProcedure"
						+ " WHERE subSequentProcedure.specialityId = :specialityId"
						+ " AND subSequentProcConfigId <> :PrimaryKey"
						+ " AND subSequentProcedure.orgId = :orgId "
						+ " AND subSequentProcedure.unitId = :unitId "
						),
})

@Entity
@Table(name = "m_sub_sequent_procedure_config" , schema = "otims")
@SequenceGenerator(name = "m_sub_sequent_procedure_config_seq" , sequenceName = "otims.m_sub_sequent_procedure_config_seq" , allocationSize = 1)
public class SubSequentProcedureConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_sub_sequent_procedure_config_seq")
	@Column(name = "sub_sequent_proc_config_id")
	private int subSequentProcConfigId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="speciality_id",insertable = false, updatable = false)
	SpecialityMaster specialityMaster;

	public int getSubSequentProcConfigId() {
		return subSequentProcConfigId;
	}

	public void setSubSequentProcConfigId(int subSequentProcConfigId) {
		this.subSequentProcConfigId = subSequentProcConfigId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}
	
	
	
}
