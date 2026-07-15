package com.param.service.global.model;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({ @NamedNativeQuery(name = "GET_CATEGORY_WISE_CONSUMABLE_DETAILS_BY_PACKAGE", query = "select "
		+ "	pccd.package_category_consumable_id as \"packageCategoryConsumableId\", "
		+ "	pccd.package_id as \"packageId\", "
		+ "	pccd.number_to_be_use as \"numberToBeUse\", "
		+ "	pccd.category_id as \"categoryId\", "
		+ "	pccd.org_id as \"orgId\", "
		+ "	pccd.org_unit_id as \"orgUnitId\", "
		+ "	pccd.parent_package_id as \"parentPackageId\", "
		+ "	pcm.category as \"productCategory\" "
		+ "from "
		+ "	service.t_package_category_wise_consumable_details pccd "
		+ "inner join public.m_product_category pcm on "
		+ "	pccd.category_id = pcm.id "
		+ "where "
		+ "	pccd.package_id = :packageId"), 
	})

@Entity
@Table(name = "t_package_category_wise_consumable_details", schema = "service")
@SequenceGenerator(name = "t_seq_package_category_wise_consumable_details", sequenceName = "service.t_seq_package_category_wise_consumable_details", allocationSize = 1)
public class TPackageCategoryWiseConsumableDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_package_category_wise_consumable_details")
	@Column(name = "package_category_consumable_id")
	private Integer packageCategoryConsumableId;

	@Column(name = "category_id")
	private Integer categoryId;

	@Column(name = "number_to_be_use")
	private Integer numberToBeUse;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private char status;

	@Column(name = "parent_package_id")
	private Integer parentPackageId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tPackageCategoryWiseConsumableDetails")
	private List<TPackageIncExcDetails> listTPackageIncExcDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", insertable = false, updatable = false, nullable = false)
	private MPackageMaster mPackageMaster;

	public MPackageMaster getmPackageMaster() {
		return mPackageMaster;
	}

	public void setmPackageMaster(MPackageMaster mPackageMaster) {
		this.mPackageMaster = mPackageMaster;
	}

	public List<TPackageIncExcDetails> getListTPackageIncExcDetails() {
		return listTPackageIncExcDetails;
	}

	public void setListTPackageIncExcDetails(List<TPackageIncExcDetails> listTPackageIncExcDetails) {
		this.listTPackageIncExcDetails = listTPackageIncExcDetails;
	}

	public Integer getPackageCategoryConsumableId() {
		return packageCategoryConsumableId;
	}

	public void setPackageCategoryConsumableId(Integer packageCategoryConsumableId) {
		this.packageCategoryConsumableId = packageCategoryConsumableId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getNumberToBeUse() {
		return numberToBeUse;
	}

	public void setNumberToBeUse(Integer numberToBeUse) {
		this.numberToBeUse = numberToBeUse;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

}
