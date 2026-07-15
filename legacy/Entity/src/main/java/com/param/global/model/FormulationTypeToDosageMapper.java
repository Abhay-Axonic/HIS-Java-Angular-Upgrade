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


@Entity
@Table(name="m_fomulation_dosage_mapper",schema="public")
@SequenceGenerator(name = "fomulation_dosage_mapper_seq", sequenceName = "public.fomulation_dosage_mapper_seq", allocationSize = 1)
public class FormulationTypeToDosageMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fomulation_dosage_mapper_seq")
	@Column(name = "formulation_dosage_mapp_id")
	private Integer formulationDosageMappId;

	@Column(name = "formulation_type_id")
	private Integer formulationTypeId;

	@Column(name = "factor_id")
	private Integer factorId;
	
	@Column(name = "dosage_form")
    private String dosageForm;
	
	@Column(name = "from_conversion_factor")	
	private Double fromConversionFactor;
	
	@Column(name = "from_conversion_factor_unit_id")
	private Integer fromConversionFactorUnit;
	
	@Column(name = "to_conversion_factor")
	private Double toConversionFactor;
	
	@Column(name = "to_conversion_factor_unit_id")
	private Integer toConversionFactorUnit;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;

	public Integer getFormulationDosageMappId() {
		return formulationDosageMappId;
	}

	public void setFormulationDosageMappId(Integer formulationDosageMappId) {
		this.formulationDosageMappId = formulationDosageMappId;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Integer getFactorId() {
		return factorId;
	}

	public void setFactorId(Integer factorId) {
		this.factorId = factorId;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public Double getFromConversionFactor() {
		return fromConversionFactor;
	}

	public void setFromConversionFactor(Double fromConversionFactor) {
		this.fromConversionFactor = fromConversionFactor;
	}

	public Integer getFromConversionFactorUnit() {
		return fromConversionFactorUnit;
	}

	public void setFromConversionFactorUnit(Integer fromConversionFactorUnit) {
		this.fromConversionFactorUnit = fromConversionFactorUnit;
	}

	public Double getToConversionFactor() {
		return toConversionFactor;
	}

	public void setToConversionFactor(Double toConversionFactor) {
		this.toConversionFactor = toConversionFactor;
	}

	public Integer getToConversionFactorUnit() {
		return toConversionFactorUnit;
	}

	public void setToConversionFactorUnit(Integer toConversionFactorUnit) {
		this.toConversionFactorUnit = toConversionFactorUnit;
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
	
	
}
