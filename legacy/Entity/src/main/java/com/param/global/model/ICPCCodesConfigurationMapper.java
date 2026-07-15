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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({

	@NamedQuery(name = "GET_SPECIALITY_LIST_BY_SYSTEM_ID",
			query="SELECT mapper.icpcCodeConfigurationMpprId AS icpcCodeConfigurationMpprId, "
						+ " mapper.specialityId AS specialityId,  "
						+ " specialityMaster.specialityName AS specialityName "
						+ " FROM ICPCCodesConfigurationMapper mapper "
						+ " INNER JOIN mapper.specialityMaster specialityMaster "
						+ " WHERE mapper.organizationId= :organizationId "
						+ " AND mapper.unitId=:unitId "
						+ " AND mapper.status= :status "
						+ " AND mapper.icpcConfigurationId= :icpcConfigurationId"),
	
	@NamedQuery(name="GET_ICPC_SYSTEM_LIST_BY_SPECIALITY_ID",
	query="SELECT distinct icpcMstr.icpcSystemDesc AS systemName, " 
			+" mstr.icpcSystemId AS icpcSystemId "
			+" FROM ICPCCodesConfigurationMapper mapper " 
			+" inner join mapper.iCPCCodeConfiguration mstr "
			+" inner join mstr.icpcSystemMaster icpcMstr "
			+ " WHERE mapper.organizationId= :organizationId "
			+ " AND mapper.unitId=:unitId "
			+ " AND mapper.status= 'A' "
			+" AND mstr.status = 'A' ")
			//+" order by icpcMstr.icpcSystemDesc asc ")
})

@Entity
@Table(name = "t_icpc_code_configuration_mapper", schema = "adt")
@SequenceGenerator(name = "t_icpc_code_configuration_mapper_seq", sequenceName = "adt.t_icpc_code_configuration_mapper_seq", allocationSize = 1)
public class ICPCCodesConfigurationMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_icpc_code_configuration_mapper_seq")
	@Column(name = "icpc_code_configuration_mppr_id")
	private Integer icpcCodeConfigurationMpprId;
	
	@Column(name="icpc_configuration_id")
	private Integer icpcConfigurationId;

	@Column(name="speciality_id")
	private Integer specialityId;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icpc_configuration_id", insertable = false, updatable = false)
	private ICPCCodeConfiguration iCPCCodeConfiguration ;
	
 	public Integer getIcpcCodeConfigurationMpprId() {
		return icpcCodeConfigurationMpprId;
	}

	public void setIcpcCodeConfigurationMpprId(Integer icpcCodeConfigurationMpprId) {
		this.icpcCodeConfigurationMpprId = icpcCodeConfigurationMpprId;
	}


	public Integer getIcpcConfigurationId() {
		return icpcConfigurationId;
	}

	public void setIcpcConfigurationId(Integer icpcConfigurationId) {
		this.icpcConfigurationId = icpcConfigurationId;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

}
