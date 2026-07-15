package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.IcpcSystemMaster;

@NamedQueries({

	@NamedQuery(name = "CHECK_FOR_SYSTEM_ID",
	query="SELECT mapper.icpcCodeConfigurationId AS icpcCodeConfigurationId , mapper.icpcSystemId AS icpcSystemId "
				+ " FROM ICPCCodeConfiguration mapper "
				+ " WHERE mapper.organizationId= :organizationId "
				+ " AND mapper.unitId=:unitId "
				+ " AND mapper.icpcSystemId= :icpcSystemId"),
	
	@NamedQuery(name = "GET_ICPC_CODES_CONFIGURATION_MAPPER_LIST",
	query="SELECT distinct mapper.icpcSystemId AS icpcSystemId,  "
				+ " mapper.icpcCodeConfigurationId AS icpcCodeConfigurationId, "
				+ " mapper.status AS status, "
				+ " icpcSystemMaster.icpcSystemDesc AS systemName "
				+ " FROM ICPCCodeConfiguration mapper "
				+ " INNER JOIN mapper.icpcSystemMaster icpcSystemMaster "
				+ " WHERE mapper.organizationId= :organizationId "
				+ " AND mapper.unitId=:unitId "),
			//	+ " AND mapper.status= :status"),
})

@Entity
@Table(name = "t_icpc_code_configuration", schema = "adt")
@SequenceGenerator(name = "icpc_code_configuration_seq", sequenceName = "adt.icpc_code_configuration_seq", allocationSize = 1)
public class ICPCCodeConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icpc_code_configuration_seq")
	@Column(name = "icpc_code_configuration_id")
	private Integer icpcCodeConfigurationId;
	
	@Column(name="icpc_system_id")
	private Integer icpcSystemId;

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
	@JoinColumn(name = "icpc_system_id", insertable = false, updatable = false)
	private IcpcSystemMaster icpcSystemMaster ;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "iCPCCodeConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ICPCCodesConfigurationMapper> iCPCCodesConfigurationMapper;

	public Integer getIcpcCodeConfigurationId() {
		return icpcCodeConfigurationId;
	}

	public void setIcpcCodeConfigurationId(Integer icpcCodeConfigurationId) {
		this.icpcCodeConfigurationId = icpcCodeConfigurationId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
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
