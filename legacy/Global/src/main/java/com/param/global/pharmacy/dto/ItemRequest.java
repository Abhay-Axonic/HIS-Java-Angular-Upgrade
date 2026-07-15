package com.param.global.pharmacy.dto;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class ItemRequest extends CommonBillingItemsDto{

	protected Integer balanceQuantity;

	protected Integer orderedQuantity;

	protected Double leaseRate;

	protected Integer orderDetailId;

	protected Integer statusId;

	protected Long saleDetailId;

	protected Integer id;

	protected String validationMessage;
	
	protected Integer creditItemQuantity;
	
	protected Double dose;
	
	protected Integer doseUnitId;
	
	protected Integer frequencyId;
	
	private Integer issueId;
	
	private Integer issueDetailId;

	private Integer parentIssueDetailsId;
	
	private Boolean isFromAssignPicker;
	
	private Integer parentItemId;
	
	private Boolean isFromOt;
	
	private Integer batchAllocationId;
	private Integer infusionSolutionId;
	
	private boolean barcodeScanned;

	public boolean getBarcodeScanned() {
		return barcodeScanned;
	}

	public void setBarcodeScanned(boolean barcodeScanned) {
		this.barcodeScanned = barcodeScanned;
	}

	public Integer getBatchAllocationId() {
		return batchAllocationId;
	}

	public void setBatchAllocationId(Integer batchAllocationId) {
		this.batchAllocationId = batchAllocationId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Double getLeaseRate() {
		return leaseRate;
	}

	public void setLeaseRate(Double leaseRate) {
		this.leaseRate = leaseRate;
	}


	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}


	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}


	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Long getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public Boolean ValidateSave() {
		Boolean result = true;
		this.validationMessage = " ";
		if (this.batchId == null || this.batchId < 0) {
			result = false;
			this.validationMessage += "batchId |";
		}
		if (this.taxId == null || this.taxId < 1) {
			result = false;
			this.validationMessage += "taxId |";
		}
		if (this.taxPercentage == null || this.taxPercentage < 0) {
			result = false;
			this.validationMessage += "taxPercentage |";
		}
		if (this.taxAmount == null || this.taxAmount < 0) {
			result = false;
			this.validationMessage += "taxAmount |";
		}
		if (this.quantity == null || this.quantity < 1) {
			result = false;
			this.validationMessage += "quantity |";
		}
		if (this.netAmount == null || this.netAmount < 0) {
			result = false;
			this.validationMessage += "netAmount |";
		}
		if (this.totalAmount == null || this.totalAmount < 0) {
			result = false;
			this.validationMessage += "totalAmount |";
		}
		if (this.uomTypeId == null || this.uomTypeId < 1) {
			result = false;
			this.validationMessage += "uomTypeId |";
		}
		if (this.uomUnitId == null || this.uomUnitId < 1) {
			result = false;
			this.validationMessage += "uomUnitId |";
		}
		return result;
	}

	public Integer getCreditItemQuantity() {
		return creditItemQuantity;
	}

	public void setCreditItemQuantity(Integer creditItemQuantity) {
		this.creditItemQuantity = creditItemQuantity;
	}


	public Integer getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(Integer balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}


	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getParentIssueDetailsId() {
		return parentIssueDetailsId;
	}

	public void setParentIssueDetailsId(Integer parentIssueDetailsId) {
		this.parentIssueDetailsId = parentIssueDetailsId;
	}

	public Boolean getIsFromAssignPicker() {
		return isFromAssignPicker;
	}

	public void setIsFromAssignPicker(Boolean isFromAssignPicker) {
		this.isFromAssignPicker = isFromAssignPicker;
	}

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public Boolean getIsFromOt() {
		return isFromOt;
	}

	public void setIsFromOt(Boolean isFromOt) {
		this.isFromOt = isFromOt;
	}

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

}