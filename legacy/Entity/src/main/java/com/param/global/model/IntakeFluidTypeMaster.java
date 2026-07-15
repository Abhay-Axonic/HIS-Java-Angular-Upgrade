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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_INTAKE_FLUID_TYPE_MASTER_LIST",
		query="SELECT intakeFluidTypeMaster.intakeTypeFluidId AS intakeTypeFluidId, intakeFluidTypeMaster.intakeFluidId AS intakeFluidId,intakeFluidTypeMaster.intakeTypeFluidCode AS intakeTypeFluidCode, intakeFluidTypeMaster.intakeTypeFluidDesc AS intakeTypeFluidDesc,"
					+ " intakeFluidTypeMaster.organizationId AS organizationId, intakeFluidTypeMaster.unitId AS unitId, intakeFluidTypeMaster.status AS status"
					+ " FROM IntakeFluidTypeMaster intakeFluidTypeMaster"
					+ " WHERE  "
					+ " intakeFluidTypeMaster.intakeFluidId= :intakeFluidId"
					+ " ORDER BY intakeFluidTypeMaster.intakeTypeFluidDesc asc"
					)
	})
@Entity
@Table(name = "m_intake_type_Fluid_master", schema = "public")
@SequenceGenerator(name = "m_intake_type_Fluid_master_seq", sequenceName = "public.m_intake_type_Fluid_master_seq", allocationSize = 1)

public class IntakeFluidTypeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_intake_type_Fluid_master_seq")
	
	@Column(name = "intake_type_Fluid_id")
	private Integer intakeTypeFluidId;
	
	@Column(name = "intake_type_Fluid_code")
	private String intakeTypeFluidCode;
	
	@Column(name = "intake_type_Fluid_desc")
	private String intakeTypeFluidDesc;
	
	@Column(name = "intake_Fluid_id")
	private Integer intakeFluidId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "intakeFluidMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IntakeOutputChartDetailsMaster> intakeOutputChartDetailsMasterList;
	


	public Integer getIntakeFluidId() {
		return intakeFluidId;
	}

	public void setIntakeFluidId(Integer intakeFluidId) {
		this.intakeFluidId = intakeFluidId;
	}


	public Integer getIntakeTypeFluidId() {
		return intakeTypeFluidId;
	}

	public void setIntakeTypeFluidId(Integer intakeTypeFluidId) {
		this.intakeTypeFluidId = intakeTypeFluidId;
	}

	public String getIntakeTypeFluidCode() {
		return intakeTypeFluidCode;
	}

	public void setIntakeTypeFluidCode(String intakeTypeFluidCode) {
		this.intakeTypeFluidCode = intakeTypeFluidCode;
	}

	public String getIntakeTypeFluidDesc() {
		return intakeTypeFluidDesc;
	}

	public void setIntakeTypeFluidDesc(String intakeTypeFluidDesc) {
		this.intakeTypeFluidDesc = intakeTypeFluidDesc;
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
