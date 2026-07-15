package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_contract_pharmacy_exclusion" , schema = "service")
@SequenceGenerator(name = "t_contract_pharmacy_exclusion_seq" , sequenceName = "service.t_contract_pharmacy_exclusion_seq" , allocationSize = 1)
public class TContractPharmacyExclusion {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_contract_pharmacy_exclusion_seq")
	@Column(name = "contract_pharmacy_exclusion_id")
	private Integer contractPharmacyExclusionId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "product_category_id")
	private Integer productCategoryId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "organization_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "status")
	private Character status;

	public Integer getContractPharmacyExclusionId() {
		return contractPharmacyExclusionId;
	}

	public void setContractPharmacyExclusionId(Integer contractPharmacyExclusionId) {
		this.contractPharmacyExclusionId = contractPharmacyExclusionId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
