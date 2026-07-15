package com.param.service.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.pharmacy.SaleDetail;

@Entity
@Table(name = "t_sale_detail_package_mapper", schema = "public")
public class SaleDetailPackageMapper {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "rate")
	private BigDecimal rate;

	@Column(name = "package_amount")
	private BigDecimal packageAmount;

	@Column(name = "package_qty")
	private Integer packageQty;

	@Column(name = "status")
	private Character status;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "is_package_item")
	private char isPackageItem;
	
	@Column(name = "cap_or_qty")
	private int capOrQty;  

	@Column(name = "package_consumption_cap_details_id")
	private Integer packageConsumptionCapDetailsId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "pacakge_consumption_service_detail_id")
	private Integer pacakgeConsumptionServiceDetailId;

	@Column(name = "package_consumtion_eo_detail_id")
	private Integer packageConsumptionEODetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_sale_details_id")
	private SaleDetail saleDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(BigDecimal packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public char getIsPackageItem() {
		return isPackageItem;
	}

	public void setIsPackageItem(char isPackageItem) {
		this.isPackageItem = (isPackageItem == '\u0000') ? 'N' : isPackageItem;
	}

	public Integer getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}

	public Integer getPackageConsumptionCapDetailsId() {
		return packageConsumptionCapDetailsId;
	}

	public void setPackageConsumptionCapDetailsId(Integer packageConsumptionCapDetailsId) {
		this.packageConsumptionCapDetailsId = packageConsumptionCapDetailsId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getPacakgeConsumptionServiceDetailId() {
		return pacakgeConsumptionServiceDetailId;
	}

	public void setPacakgeConsumptionServiceDetailId(Integer pacakgeConsumptionServiceDetailId) {
		this.pacakgeConsumptionServiceDetailId = pacakgeConsumptionServiceDetailId;
	}

	public Integer getPackageConsumptionEODetailId() {
		return packageConsumptionEODetailId;
	}

	public void setPackageConsumptionEODetailId(Integer packageConsumptionEODetailId) {
		this.packageConsumptionEODetailId = packageConsumptionEODetailId;
	}

	public SaleDetail getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(SaleDetail saleDetail) {
		this.saleDetail = saleDetail;
	}

	public int getCapOrQty() {
		return capOrQty;
	}

	public void setCapOrQty(int capOrQty) {
		this.capOrQty = capOrQty;
	}
	
	

}
