package com.param.procurement.purchase.purchaseOrder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.master.OtherCharge;
import com.param.procurement.common.model.AbstractEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Entity(name = "PurchaseOrderOtherChargesMapper")
@Table(name = "t_other_charges_po_mapper", schema = "procurement")
public class PurchaseOrderOtherChargesMapper extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "organization_id")
	@ApiModelProperty(value = "OrganisationId Of StoreIndentMaster ")
	private Integer organizationId;

	@Column(name = "unit_id")
	@ApiModelProperty(value = "UnitId Of StoreIndentMaster")
	private Integer unitId;
	
	@Column(name = "is_active")
	@ApiModelProperty(value = "isActive Of LocalPurchaseRequest")
	private Boolean isActive;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "purchase_order_detail_id")
	private LocPurchaseOrderDetail purchaseOrderDetail;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "other_charges_id")
	private OtherCharge otherCharge;
	
	@Column(name = "amount")
	private Double amount;
	

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocPurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(LocPurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	public OtherCharge getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(OtherCharge otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
