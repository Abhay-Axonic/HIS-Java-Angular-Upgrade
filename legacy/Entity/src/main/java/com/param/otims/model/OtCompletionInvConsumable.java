package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_ot_completion_inv_consumable" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_inv_consumable_seq" , sequenceName = "otims.t_ot_completion_inv_consumable_seq" , allocationSize = 1)
public class OtCompletionInvConsumable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_inv_consumable_seq")
	@Column(name = "ot_completion_inv_consumable_id")
	private int otCompletionInvConsumableId;
	
	@Column(name = "procedure_request_id")
	private Integer procedureRequestId;

    @Column(name = "rejected_qty ")
    private Integer rejectedQty ;
    
	@Column(name = "is_item_kit")
	private char isItemKit;
	
	@Column(name = "inventory_type_id")
	private Integer inventoryTypeId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "booked_qty")
	private Integer bookedQty;
	
	@Column(name = "used_qty")
	private Integer usedQty;
	
	@Column(name = "mrp")
	private double mrp;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "tax_percentage")
	private Float taxPercentage;
	
	@Column(name = "remaining_qty")
	private Integer remainingQty;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "total_amount")
	private Float totalAmount;
	
	@Column(name = "batch_id")
	private Integer batchId;
	
	@Column(name = "uom_type_id")
	private Integer uomTypeId;
	
	@Column(name = "uom_unit_id")
	private Integer uomUnitId;

	public int getOtCompletionInvConsumableId() {
		return otCompletionInvConsumableId;
	}

	public void setOtCompletionInvConsumableId(int otCompletionInvConsumableId) {
		this.otCompletionInvConsumableId = otCompletionInvConsumableId;
	}
	
	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}

	
	public char getIsItemKit() {
		return isItemKit;
	}

	public void setIsItemKit(char isItemKit) {
		this.isItemKit = isItemKit;
	}

	public Integer getInventoryTypeId() {
		return inventoryTypeId;
	}

	public void setInventoryTypeId(Integer inventoryTypeId) {
		this.inventoryTypeId = inventoryTypeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getBookedQty() {
		return bookedQty;
	}

	public void setBookedQty(Integer bookedQty) {
		this.bookedQty = bookedQty;
	}

	public Integer getUsedQty() {
		return usedQty;
	}

	public void setUsedQty(Integer usedQty) {
		this.usedQty = usedQty;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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

	public Float getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Float taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(Integer remainingQty) {
		this.remainingQty = remainingQty;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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
	
	
}
