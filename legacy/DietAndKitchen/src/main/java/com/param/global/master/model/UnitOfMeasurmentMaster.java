package com.param.global.master.model;

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

		@NamedQuery(name = "GET_UNIT_MASTER_DETAILS_BY_NAME", query = "SELECT unitOfMeasurmentMaster.uomId as UomId ,"
				+ " unitOfMeasurmentMaster.uomName as uomName , unitOfMeasurmentMaster.uomDescription as uomDescription "
				+ " FROM UnitOfMeasurmentMaster unitOfMeasurmentMaster "
				+ " WHERE unitOfMeasurmentMaster.organizationId=:organizationId and unitOfMeasurmentMaster.unitId=:unitId"
				+ " ORDER BY unitOfMeasurmentMaster.uomDescription ASC ") })

@Entity
@Table(name = "m_unit_of_measurment_master", schema = "kitchen")
@SequenceGenerator(name = "m_uom_master_seq", sequenceName = "kitchen.m_uom_master_seq", allocationSize = 1)
public class UnitOfMeasurmentMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_uom_master_seq")
	@Column(name = "uom_id")
	private Integer uomId;

	@Column(name = "uom_name")
	private String uomName;

	@Column(name = "uom_description")
	private String uomDescription;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	// @OneToMany(mappedBy="")
	// private KeyIngredientsMaster keyIngredientsMaster;

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomDescription() {
		return uomDescription;
	}

	public void setUomDescription(String uomDescription) {
		this.uomDescription = uomDescription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	

}
