package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_tax_component_Details", schema = "pharmacy")
@SequenceGenerator(name = "tax_component_Details_seq", sequenceName = "pharmacy.tax_component_Details_seq", allocationSize = 1)
public class TaxComponentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_component_Details_seq")
	@Column(name = "tax_component_Details_id")
	private Integer taxComponentDetailsId;

	@Column(name = "grn_id")
	private Integer grnId;
	
	@Column(name = "grn_details_id")
	private Integer grnDetailsId;
	
	@Column(name = "grn_retrun_id")
	private Integer grnRetrunId;
	
	@Column(name = "grn_retrun_details_id")
	private Integer grnRetrunDetailsId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "tax_id")
	private Integer taxId;
	
	@Column(name = "tax_percentage")
	private Double taxPercentage;
	
	@Column(name = "tax_amount")
	private Double taxAmount;
	
	@Column(name = "tax_component_id")
	private Integer taxComponentId;
	
	@Column(name = "tax_component_percentage")
	private Double taxComponentPercentage;

	@Column(name = "tax_component_name")
	private String taxComponentName;
	
	@Column(name = "tax_component_amount")
	private Double taxComponentAmount;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;

	public Integer getTaxComponentDetailsId() {
		return taxComponentDetailsId;
	}

	public void setTaxComponentDetailsId(Integer taxComponentDetailsId) {
		this.taxComponentDetailsId = taxComponentDetailsId;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getGrnDetailsId() {
		return grnDetailsId;
	}

	public void setGrnDetailsId(Integer grnDetailsId) {
		this.grnDetailsId = grnDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getTaxComponentId() {
		return taxComponentId;
	}

	public void setTaxComponentId(Integer taxComponentId) {
		this.taxComponentId = taxComponentId;
	}

	public Double getTaxComponentPercentage() {
		return taxComponentPercentage;
	}

	public void setTaxComponentPercentage(Double taxComponentPercentage) {
		this.taxComponentPercentage = taxComponentPercentage;
	}

	public String getTaxComponentName() {
		return taxComponentName;
	}

	public void setTaxComponentName(String taxComponentName) {
		this.taxComponentName = taxComponentName;
	}

	public Double getTaxComponentAmount() {
		return taxComponentAmount;
	}

	public void setTaxComponentAmount(Double taxComponentAmount) {
		this.taxComponentAmount = taxComponentAmount;
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

	public Integer getGrnRetrunId() {
		return grnRetrunId;
	}

	public void setGrnRetrunId(Integer grnRetrunId) {
		this.grnRetrunId = grnRetrunId;
	}

	public Integer getGrnRetrunDetailsId() {
		return grnRetrunDetailsId;
	}

	public void setGrnRetrunDetailsId(Integer grnRetrunDetailsId) {
		this.grnRetrunDetailsId = grnRetrunDetailsId;
	}
	
	
}
