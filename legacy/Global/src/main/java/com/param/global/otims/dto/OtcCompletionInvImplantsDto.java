package com.param.global.otims.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.param.global.billing.dto.CommonBillingItemsDto;

public class OtcCompletionInvImplantsDto extends CommonBillingItemsDto{

	private Integer otcCompletionInvImplantsId;
 
	private Integer procedureRequestId;

	private Integer bookedQty;

	private Integer usedQty;

	private char status;

	private Integer orgId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private String manufacturerName;

	private Integer manufacturerId;

	private Integer remainingQty;

	private Integer rejectedQty;

	private Integer storeId;

	private String taxName;
	
	private Integer issueId;
	
	private boolean isImplant;
	
	private boolean isConsumable;

		private List<BatchDto> batch;
		
		@JsonIgnore
		private String selectedBatch;
		
		@JsonIgnore
		private String selectedUom;

		private Double taxPercent;
		
		private String itemName;
		
		private String itemCode;
		
		private String uomType;
		
		private String uomUnit;
		
		private Double mrp;
		
		private String expiryDate;
		
		private Integer issuedQty;
		
		private String storeName;

		private String batchNo;
		
		private String uhidNumber;

		private Integer salesTaxId;
		
		private Integer purchaseTaxId;
		
		private Double purchaseTaxPercentage;
		
		private Double salesTaxPercentage;
		
		private String batchDetails;
		
		private Integer issueDetailsBatchMpprId;
		
		private Boolean fromOT;
		
		private Integer allocationId;
		
		public Integer getIssueId() {
			return issueId;
		}

		public void setIssueId(Integer issueId) {
			this.issueId = issueId;
		}

		public Integer getOtcCompletionInvImplantsId() {
			return otcCompletionInvImplantsId;
		}

		public void setOtcCompletionInvImplantsId(Integer otcCompletionInvImplantsId) {
			this.otcCompletionInvImplantsId = otcCompletionInvImplantsId;
		}

		public Integer getProcedureRequestId() {
			return procedureRequestId;
		}

		public void setProcedureRequestId(Integer procedureRequestId) {
			this.procedureRequestId = procedureRequestId;
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

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}

	
		public String getManufacturerName() {
			return manufacturerName;
		}

		public void setManufacturerName(String manufacturerName) {
			this.manufacturerName = manufacturerName;
		}

		public Integer getManufacturerId() {
			return manufacturerId;
		}

		public void setManufacturerId(Integer manufacturerId) {
			this.manufacturerId = manufacturerId;
		}

		public Double getTaxPercentage() {
			return taxPercentage;
		}

		public void setTaxPercentage(Double taxPercentage) {
			this.taxPercentage = taxPercentage;
		}

		public Integer getRemainingQty() {
			return remainingQty;
		}

		public void setRemainingQty(Integer remainingQty) {
			this.remainingQty = remainingQty;
		}

		public Integer getRejectedQty() {
			return rejectedQty;
		}

		public void setRejectedQty(Integer rejectedQty) {
			this.rejectedQty = rejectedQty;
		}

		public Integer getTaxId() {
			return taxId;
		}

		public void setTaxId(Integer taxId) {
			this.taxId = taxId;
		}

		public Integer getStoreId() {
			return storeId;
		}

		public void setStoreId(Integer storeId) {
			this.storeId = storeId;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public Double getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(Double netAmount) {
			this.netAmount = netAmount;
		}

		public Double getTaxAmount() {
			return taxAmount;
		}

		public void setTaxAmount(Double taxAmount) {
			this.taxAmount = taxAmount;
		}

		public String getTaxName() {
			return taxName;
		}

		public void setTaxName(String taxName) {
			this.taxName = taxName;
		}

		public List<BatchDto> getBatch() {
			return batch;
		}

		public void setBatch(List<BatchDto> batch) {
			this.batch = batch;
		}

		public Double getTaxPercent() {
			return taxPercent;
		}

		public void setTaxPercent(Double taxPercent) {
			this.taxPercent = taxPercent;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getItemCode() {
			return itemCode;
		}

		public void setItemCode(String itemCode) {
			this.itemCode = itemCode;
		}

		public String getUomType() {
			return uomType;
		}

		public void setUomType(String uomType) {
			this.uomType = uomType;
		}

		public String getUomUnit() {
			return uomUnit;
		}

		public void setUomUnit(String uomUnit) {
			this.uomUnit = uomUnit;
		}

		public Double getMrp() {
			return mrp;
		}

		public void setMrp(Double mrp) {
			this.mrp = mrp;
		}

		public String getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Integer getIssuedQty() {
			return issuedQty;
		}

		public void setIssuedQty(Integer issuedQty) {
			this.issuedQty = issuedQty;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
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

		public String getBatchNo() {
			return batchNo;
		}

		public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}

		public String getUhidNumber() {
			return uhidNumber;
		}

		public void setUhidNumber(String uhidNumber) {
			this.uhidNumber = uhidNumber;
		}

		public Integer getSalesTaxId() {
			return salesTaxId;
		}

		public void setSalesTaxId(Integer salesTaxId) {
			this.salesTaxId = salesTaxId;
		}

		public Integer getPurchaseTaxId() {
			return purchaseTaxId;
		}

		public void setPurchaseTaxId(Integer purchaseTaxId) {
			this.purchaseTaxId = purchaseTaxId;
		}

		public Double getPurchaseTaxPercentage() {
			return purchaseTaxPercentage;
		}

		public void setPurchaseTaxPercentage(Double purchaseTaxPercentage) {
			this.purchaseTaxPercentage = purchaseTaxPercentage;
		}

		public Double getSalesTaxPercentage() {
			return salesTaxPercentage;
		}

		public void setSalesTaxPercentage(Double salesTaxPercentage) {
			this.salesTaxPercentage = salesTaxPercentage;
		}

		public String getBatchDetails() {
			return batchDetails;
		}

		public void setBatchDetails(String batchDetails) {
			this.batchDetails = batchDetails;
		}

		public boolean isImplant() {
			return isImplant;
		}

		public void setImplant(boolean isImplant) {
			this.isImplant = isImplant;
		}

		public boolean isConsumable() {
			return isConsumable;
		}

		public void setConsumable(boolean isConsumable) {
			this.isConsumable = isConsumable;
		}

		public String getSelectedBatch() {
			return selectedBatch;
		}

		public void setSelectedBatch(String selectedBatch) {
			this.selectedBatch = selectedBatch;
		}

		public String getSelectedUom() {
			return selectedUom;
		}

		public void setSelectedUom(String selectedUom) {
			this.selectedUom = selectedUom;
		}

		public Integer getIssueDetailsBatchMpprId() {
			return issueDetailsBatchMpprId;
		}

		public void setIssueDetailsBatchMpprId(Integer issueDetailsBatchMpprId) {
			this.issueDetailsBatchMpprId = issueDetailsBatchMpprId;
		}

		public Integer getProductCategoryId() {
			return productCategoryId;
		}

		public void setProductCategoryId(Integer productCategoryId) {
			this.productCategoryId = productCategoryId;
		}

		public Boolean getFromOT() {
			return fromOT;
		}
		
		public void setFromOT(Boolean fromOT) {
			this.fromOT = fromOT;
		}

		public Integer getAllocationId() {
			return allocationId;
		}

		public void setAllocationId(Integer allocationId) {
			this.allocationId = allocationId;
		}

		
		
}
