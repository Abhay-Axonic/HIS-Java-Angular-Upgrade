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

import com.param.procurement.common.model.AbstractEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Entity(name = "PurchaseOrderUomMapper")
@Table(name = "t_purchase_order_uom_mapper", schema = "procurement")
public class PurchaseOrderUomMapper extends AbstractEntity{
	
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
	
	@Column(name = "uom_type_id")
	private Integer uomTypeId;
	    
	@Column(name = "uom_unit_id")
	private Integer uomUnitId;
	    
	@Column(name = "conversions")
	private Integer conversions;
    
    @Column(name="ip_uom")
	private Boolean ipUom;
    
    @Column(name="op_uom")
	private Boolean opUom;
    
    @Column(name="store_uom")
	private Boolean storeUom;

	public PurchaseOrderUomMapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseOrderUomMapper(Integer id, Integer organizationId, Integer unitId, Boolean isActive,
			LocPurchaseOrderDetail purchaseOrderDetail, Integer uomTypeId, Integer uomUnitId, Integer conversions,
			Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.unitId = unitId;
		this.isActive = isActive;
		this.purchaseOrderDetail = purchaseOrderDetail;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getConversions() {
		return conversions;
	}

	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}

	
	public Boolean getIpUom() {
		return ipUom;
	}

	public void setIpUom(Boolean ipUom) {
		this.ipUom = ipUom;
	}

	public Boolean getOpUom() {
		return opUom;
	}

	public void setOpUom(Boolean opUom) {
		this.opUom = opUom;
	}

	public Boolean getStoreUom() {
		return storeUom;
	}

	public void setStoreUom(Boolean storeUom) {
		this.storeUom = storeUom;
	}

	
	   
	    
}
