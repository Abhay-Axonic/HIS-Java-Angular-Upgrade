package com.param.entity.model.procurement;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "t_new_work_order_creation_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_creation_details_seq", sequenceName = "procurement.t_new_work_order_creation_details_seq", allocationSize = 1)

public class NewWorkOrderCreationDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_creation_details_seq")
	@Column(name = "workorder_request_details_id")
	private Integer workorderRequestDetialsId;
	
	@Column(name = "workorder_request_id")
	private Integer workorderRequestId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_quantity")
	private Integer itemQuantity;
	
	@Column(name = "item_rate")
	private Double itemRate;
	
	@Column(name = "item_mrp")
	private Double itemMrp;
	
	@Column(name = "item_disc_percentage")
	private Double itemDiscPercentage;
	
	@Column(name = "item_disc_amount")
	private Double itemDiscAmount;
	
	@Column(name = "item_tax_id")
	private Integer itemTaxId;
	
	@Column(name = "item_tax")
	private String itemTax;
	
	@Column(name = "item_tax_amount")
	private Double itemTaxAmount;
	
	@Column(name = "item_net_amount")
	private Double itemNetAmount;
	
	@Column(name = "item_remark")
	private String itemRemark;
	
	@Column(name = "org_id")
	private Integer orgId;
	
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
	private String status;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getWorkorderRequestDetialsId() {
		return workorderRequestDetialsId;
	}

	public void setWorkorderRequestDetialsId(Integer workorderRequestDetialsId) {
		this.workorderRequestDetialsId = workorderRequestDetialsId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Double getItemRate() {
		return itemRate;
	}

	public void setItemRate(Double itemRate) {
		this.itemRate = itemRate;
	}

	public Double getItemMrp() {
		return itemMrp;
	}

	public void setItemMrp(Double itemMrp) {
		this.itemMrp = itemMrp;
	}

	public Double getItemDiscPercentage() {
		return itemDiscPercentage;
	}

	public void setItemDiscPercentage(Double itemDiscPercentage) {
		this.itemDiscPercentage = itemDiscPercentage;
	}

	public Double getItemDiscAmount() {
		return itemDiscAmount;
	}

	public void setItemDiscAmount(Double itemDiscAmount) {
		this.itemDiscAmount = itemDiscAmount;
	}

	public Integer getItemTaxId() {
		return itemTaxId;
	}

	public void setItemTaxId(Integer itemTaxId) {
		this.itemTaxId = itemTaxId;
	}

	public String getItemTax() {
		return itemTax;
	}

	public void setItemTax(String itemTax) {
		this.itemTax = itemTax;
	}

	public Double getItemTaxAmount() {
		return itemTaxAmount;
	}

	public void setItemTaxAmount(Double itemTaxAmount) {
		this.itemTaxAmount = itemTaxAmount;
	}

	public Double getItemNetAmount() {
		return itemNetAmount;
	}

	public void setItemNetAmount(Double itemNetAmount) {
		this.itemNetAmount = itemNetAmount;
	}

	public String getItemRemark() {
		return itemRemark;
	}

	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
	

	
}
