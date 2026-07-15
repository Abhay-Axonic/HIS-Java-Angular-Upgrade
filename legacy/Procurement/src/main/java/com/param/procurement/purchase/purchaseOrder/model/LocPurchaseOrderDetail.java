package com.param.procurement.purchase.purchaseOrder.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.procurement.common.model.AbstractEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Entity(name = "LocPurchaseOrderDetail")
@Table(name = "t_purchase_order_detail", schema = "procurement")
public class LocPurchaseOrderDetail extends AbstractEntity {

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	private LocPurchaseOrder locPurchaseOrder;

	
   /* //purchase_order_id integer NOT NULL,
    item_id integer NOT NULL,
    po_quantity integer NOT NULL,
    bonus_quantity integer NOT NULL,
    cop double precision NOT NULL,
    mrp double precision NOT NULL,
    discount double precision NOT NULL,
    */

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "po_quantity")
	private Integer poQuantity;

	@Column(name = "bonus_quantity")
	private Integer bonusQuantity;

	/*
	 * cop double precision NOT NULL, mrp double precision NOT NULL,
	 */
	@Column(name = "cop")
	private Double cop;

	@Column(name = "mrp")
	private Double mrp;

	@Column(name = "discount")
	private Double discount;

	/*
	 * tax_id integer NOT NULL,
    tax_percentage double precision NOT NULL,
    total_amount double precision NOT NULL,
    total_discount double precision NOT NULL,
    tax_amount double precision NOT NULL,
    total_other_amount double precision NOT NULL,
    net_amount double precision NOT NULL,
    manufacturer_id integer NOT NULL,
    
	 */

	@Column(name = "tax_id")
	private Integer taxId;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "total_discount")
	private Double totalDiscount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_other_amount")
	private Double totalOtherAmount;

	@Column(name = "net_amount")
	private Double netAmount;	

	@Column(name = "manufacturer_id")
	private Integer manufactureId;

	@Column(name = "specification")
	private String specification;

	@Column(name = "remark")
	private String remark;

	@Column(name = "is_active")
	private Boolean isActive;

	/*specification character varying(100) COLLATE pg_catalog."default",
    remark character varying(100) COLLATE pg_catalog."default",
    is_active boolean NOT NULL DEFAULT true,*/

	/*
	 * po_uom_type_id integer NOT NULL,
    po_uom_unit_id integer NOT NULL,
    bonus_uom_type_id integer NOT NULL,
    bonus_uom_unit_id integer NOT NULL,
    grn_detail_id integer,
	 */

	@Column(name = "po_uom_type_id")
	private Integer poUOMTypeId;

	@Column(name = "po_uom_unit_id")
	private Integer poUOMUnitId;

	@Column(name = "bonus_uom_type_id")
	private Integer bonusUOMTypeId;

	@Column(name = "bonus_uom_unit_id")
	private Integer bonusUOMUnitId;

	@Column(name = "grn_detail_id")
	private Integer grnDetailId;
	
	/*po_approved_quantity integer,
    bonus_approved_quantity integer,
    approved_by integer,
    approved_date timestamp without time zone,
    status_id integer,
    rejected_quantity integer,
    reject_reason_id integer,
    reject_note character varying(100) COLLATE pg_catalog."default",*/
	

	@Column(name = "po_approved_quantity")
	private Integer poApprovedQuantity;
	
	@Column(name = "bonus_approved_quantity")
	private Integer bonusApprovedQuantity;
	
	@Column(name = "approved_by")
	private Integer approvedBy;
	
	@Column(name="approved_date")
	private Date approvedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;
	
	@Column(name = "reject_reason_id")
	private Integer rejectReasonId;
	
	@Column(name = "reject_note")
	private String rejectNote;

	public LocPurchaseOrderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public LocPurchaseOrderDetail(Integer id, Integer organizationId, Integer unitId, LocPurchaseOrder locPurchaseOrder,
			Integer itemId, Integer poQuantity, Integer bonusQuantity, Double cop, Double mrp, Double discount,
			Integer taxId, Double taxPercentage, Double totalAmount, Double totalDiscount, Double taxAmount,
			Double totalOtherAmount, Double netAmount, Integer manufactureId, String specification, String remark,
			Boolean isActive, Integer poUOMTypeId, Integer poUOMUnitId, Integer bonusUOMTypeId, Integer bonusUOMUnitId,
			Integer grnDetailId, Integer poApprovedQuantity, Integer bonusApprovedQuantity, Integer approvedBy,
			Date approvedDate, Status status, Integer rejectedQuantity, Integer rejectReasonId, String rejectNote) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.unitId = unitId;
		this.locPurchaseOrder = locPurchaseOrder;
		this.itemId = itemId;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.taxAmount = taxAmount;
		this.totalOtherAmount = totalOtherAmount;
		this.netAmount = netAmount;
		this.manufactureId = manufactureId;
		this.specification = specification;
		this.remark = remark;
		this.isActive = isActive;
		this.poUOMTypeId = poUOMTypeId;
		this.poUOMUnitId = poUOMUnitId;
		this.bonusUOMTypeId = bonusUOMTypeId;
		this.bonusUOMUnitId = bonusUOMUnitId;
		this.grnDetailId = grnDetailId;
		this.poApprovedQuantity = poApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.status = status;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
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

	public LocPurchaseOrder getLocPurchaseOrder() {
		return locPurchaseOrder;
	}

	public void setLocPurchaseOrder(LocPurchaseOrder locPurchaseOrder) {
		this.locPurchaseOrder = locPurchaseOrder;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Integer poQuantity) {
		this.poQuantity = poQuantity;
	}

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
	}

	public Integer getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getPoUOMTypeId() {
		return poUOMTypeId;
	}

	public void setPoUOMTypeId(Integer poUOMTypeId) {
		this.poUOMTypeId = poUOMTypeId;
	}

	public Integer getPoUOMUnitId() {
		return poUOMUnitId;
	}

	public void setPoUOMUnitId(Integer poUOMUnitId) {
		this.poUOMUnitId = poUOMUnitId;
	}

	public Integer getBonusUOMTypeId() {
		return bonusUOMTypeId;
	}

	public void setBonusUOMTypeId(Integer bonusUOMTypeId) {
		this.bonusUOMTypeId = bonusUOMTypeId;
	}

	public Integer getBonusUOMUnitId() {
		return bonusUOMUnitId;
	}

	public void setBonusUOMUnitId(Integer bonusUOMUnitId) {
		this.bonusUOMUnitId = bonusUOMUnitId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	
	
}
