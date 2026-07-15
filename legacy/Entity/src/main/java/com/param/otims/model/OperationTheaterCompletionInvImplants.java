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
@Table(name="t_ot_completion_inv_implants" , schema="otims")
@SequenceGenerator(name="t_ot_completion_inv_implants_seq" , sequenceName="otims.t_ot_completion_inv_implants_seq" , allocationSize=1)
public class OperationTheaterCompletionInvImplants {

	@Id
	@GeneratedValue(generator="t_ot_completion_inv_implants_seq" , strategy=GenerationType.SEQUENCE)
	@Column(name="ot_completion_inv_implants_id")
	private Integer otCompletionInvImplantsId;
	
	@Column(name="procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name="is_item_kit")
	private char isItemKit;
	
	@Column(name="inventory_type_id")
	private Integer inventoryTypeId;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="issued_qty")
	private Integer issuedQty;
	
	@Column(name="remaining_qty")
	private Integer remainingQty;
	
	@Column(name="used_qty")
	private Integer usedQty;
	
	@Column(name="mrp")
	private double mrp;
	
	@Column(name="status")
	private char status;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;

	public Integer getOtCompletionInvImplantsId() {
		return otCompletionInvImplantsId;
	}

	public void setOtCompletionInvImplantsId(Integer otCompletionInvImplantsId) {
		this.otCompletionInvImplantsId = otCompletionInvImplantsId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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

	public Integer getIssuedQty() {
		return issuedQty;
	}

	public void setIssuedQty(Integer issuedQty) {
		this.issuedQty = issuedQty;
	}

	public Integer getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(Integer remainingQty) {
		this.remainingQty = remainingQty;
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
		this.status = status;
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
	
	
}

