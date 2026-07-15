package com.param.global.pharmacy.dto;

import java.util.Date;
import java.util.List;

import com.param.entity.model.procurement.NewWorkOrderCompletionStagedDetailsMapper;
import com.param.entity.model.procurement.NewWorkOrderCreationStagedDetailsMapper;

public class WorkOrderCreationRequestApprovalDetailsDto {
	private Integer workorderRequestApprovalDetialsId;
	private Integer workorderRequestDetialsId;
	private Integer workorderRequestId;
	private Integer itemId;
	private Integer storeId;
	private String itemName;
	private Integer itemQuantity;
	private Double itemRate;
	private Double itemMrp;
	private Double itemDiscPercentage;
	private Double itemDiscAmount;
	private Integer itemTaxId;
	private String itemTax;
	private Double itemTaxAmount;
	private Double itemNetAmount;
	private String itemRemark;
	private String status;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private Integer unitId;
	private Integer orgId;
	
	private Object taxpercentObj;
	
	private Integer approvedQty;
	private Integer rejectedQty;
	private String approvalDetailRemark;
	private Boolean isRejectedQty;
	private Boolean isSelected;
    private List<NewWorkOrderCreationStagedDetailsMapper> workOrderCreationStagedListData; 
	private  WorkOrderRejectQuantityDetailsDto itemRejectionObject;
	private  boolean workOrderCompletion;
	private  Integer workOrderCompletionId;
	private List<NewWorkOrderCompletionStagedDetailsMapper> workOrderCompletionStagedListData; 
	private Date invoiceDate;
	private String invoiceNumber;
	private boolean isReverseTax;
	private  Integer workOrderCompletionDetailsId;
	private  String serviceName;
	private  Integer orderDetailsId;
	private  Date dueDate;
	
	
	
	
//	
//	itemRejectionObject
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Object getTaxpercentObj() {
		return taxpercentObj;
	}
	public void setTaxpercentObj(Object taxpercentObj) {
		this.taxpercentObj = taxpercentObj;
	}
	public Integer getWorkorderRequestDetialsId() {
		return workorderRequestDetialsId;
	}
	public void setWorkorderRequestDetialsId(Integer workorderRequestDetialsId) {
		this.workorderRequestDetialsId = workorderRequestDetialsId;
	}
	public Integer getWorkorderRequestApprovalDetialsId() {
		return workorderRequestApprovalDetialsId;
	}
	public void setWorkorderRequestApprovalDetialsId(Integer workorderRequestApprovalDetialsId) {
		this.workorderRequestApprovalDetialsId = workorderRequestApprovalDetialsId;
	}
	public Integer getApprovedQty() {
		return approvedQty;
	}
	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}
	public Integer getRejectedQty() {
		return rejectedQty;
	}
	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}
	public String getApprovalDetailRemark() {
		return approvalDetailRemark;
	}
	public void setApprovalDetailRemark(String approvalDetailRemark) {
		this.approvalDetailRemark = approvalDetailRemark;
	}
	public Boolean getIsRejectedQty() {
		return isRejectedQty;
	}
	public void setIsRejectedQty(Boolean isRejectedQty) {
		this.isRejectedQty = isRejectedQty;
	}
	public WorkOrderRejectQuantityDetailsDto getItemRejectionObject() {
		return itemRejectionObject;
	}
	public void setItemRejectionObject(WorkOrderRejectQuantityDetailsDto itemRejectionObject) {
		this.itemRejectionObject = itemRejectionObject;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	public List<NewWorkOrderCreationStagedDetailsMapper> getWorkOrderCreationStagedListData() {
		return workOrderCreationStagedListData;
	}
	public void setWorkOrderCreationStagedListData(List<NewWorkOrderCreationStagedDetailsMapper> workOrderCreationStagedListData) {
		this.workOrderCreationStagedListData = workOrderCreationStagedListData;
	}
	public boolean getWorkOrderCompletion() {
		return workOrderCompletion;
	}
	public void setWorkOrderCompletion(boolean workOrderCompletion) {
		this.workOrderCompletion = workOrderCompletion;
	}
	public Integer getWorkOrderCompletionId() {
		return workOrderCompletionId;
	}
	public void setWorkOrderCompletionId(Integer workOrderCompletionId) {
		this.workOrderCompletionId = workOrderCompletionId;
	}
	public List<NewWorkOrderCompletionStagedDetailsMapper> getWorkOrderCompletionStagedListData() {
		return workOrderCompletionStagedListData;
	}
	public void setWorkOrderCompletionStagedListData(List<NewWorkOrderCompletionStagedDetailsMapper> workOrderCompletionStagedListData) {
		this.workOrderCompletionStagedListData = workOrderCompletionStagedListData;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public boolean getIsReverseTax() {
		return isReverseTax;
	}
	public void setIsReverseTax(boolean isReverseTax) {
		this.isReverseTax = isReverseTax;
	}
	public Integer getWorkOrderCompletionDetailsId() {
		return workOrderCompletionDetailsId;
	}
	public void setWorkOrderCompletionDetailsId(Integer workOrderCompletionDetailsId) {
		this.workOrderCompletionDetailsId = workOrderCompletionDetailsId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
	

	 
	 
	 
	 
	 
	 
	 
	 
	 
	
	

}
