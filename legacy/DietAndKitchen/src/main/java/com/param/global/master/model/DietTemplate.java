package com.param.global.master.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.DietTypeMaster;

@Entity
@Table(name = "m_diet_template", schema = "kitchen")
@SequenceGenerator(name = "m_diet_template_diet_template_id_seq1", sequenceName = "kitchen.m_diet_template_diet_template_id_seq1", allocationSize = 1)
public class DietTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_diet_template_diet_template_id_seq1")
	@Column(name = "diet_template_id")
	private Integer dietTemplateId;

	@Column(name = "diet_type_id")
	private Integer dietTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
	private DietTypeMaster dietTypeMaster;

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
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

	public DietTypeMaster getDietTypeMaster() {
		return dietTypeMaster;
	}

	public void setDietTypeMaster(DietTypeMaster dietTypeMaster) {
		this.dietTypeMaster = dietTypeMaster;
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
