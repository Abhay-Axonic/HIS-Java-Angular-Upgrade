package com.param.entity.model.pharmacy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_PHARMACY_TARIFF_LIST",query="select tariff.id as \"id\", "
			+ "tariff.organization_id as \"organizationId\","
			+ "tariff.unit_id as \"unitId\","
			+ "tariff.asset_category_id as \"assetCategoryId\", "
			+ "acat.category as \"assetCategoryName\" , "
			+ "tariff.product_category_id as \"productCategoryId\", "
			+ "pcat.category as \"productCategoryName\", "
			+ "tariff.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "tariff.mark_up_down as \"markUpDown\", "
			+ "tariff.mark_up_down_per as \"markUpDownPer\", "
			+ "tariff.status as \"status\" "
			+ "FROM pharmacy.t_pharmacy_tariff tariff "
			+ "INNER JOIN m_product_category pcat on pcat.id = tariff.product_category_id "
			+ "INNER JOIN m_asset_category acat on acat.id = tariff.asset_category_id "
			+ "WHERE tariff.organization_id =:orgId "
			+ "AND tariff.unit_id=:unitId ")
})


@Entity
@Table(name="t_pharmacy_tariff",schema="pharmacy")
@SequenceGenerator(name = "pharmacy_tariff", sequenceName = "pharmacy.pharmacy_tariff", allocationSize = 1)
public class PharmacyTariff {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "pharmacy_tariff")
	private Integer id;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="asset_category_id")
	private Integer assetCategoryId;
	
	@Column(name="product_category_id")
	private Integer productCategoryId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="mark_up_down")
	private Integer markUpDown;
	
	@Column(name="mark_up_down_per")
	private Double markUpDownPer;
	
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
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