package com.param.pharmacy.inPatient.dto.patientConsumableIssue;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class SubstitutePatientConsumableDto {
	private Integer issueDetailId;

	private Integer saleDetailId;

	private Integer issueId;

	private String itemName;
	private String genericName;
	private String itemCode;

	private Integer patientIndentDetailId;
	private Integer statusId;
	private Integer issueQuantity;
	private Integer batchId;
	private Integer itemId;

	private String uomType;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer indentQuantity;

	private List<BatchResponse> batch;
	private List<BatchResponse> newBatch;


	private Double netAmount;
	private Double creditPayable;
	private Double selfPayable;
	private Double discountAmount;
	private Double rate;
	private Double taxAmount;
	private Double taxPercentage;
	private Double totalAmount;
	private Double unitDiscountAmount;
	private Double packageConsumptionAmount;
	private Double concessionAmount;
	private Double unitConsessionAmount;
	private Integer availableStockQuantity;

	private Integer parentIssueDetailId;
	private Boolean isSubstitute;
	private Boolean isNarcoticSubstitute;
	public Integer getIssueDetailId() {
		return issueDetailId;
	}
	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}
	public Integer getSaleDetailId() {
		return saleDetailId;
	}
	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}
	
	public Integer getIssueId() {
		return issueId;
	}
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getPatientIndentDetailId() {
		return patientIndentDetailId;
	}
	public void setPatientIndentDetailId(Integer patientIndentDetailId) {
		this.patientIndentDetailId = patientIndentDetailId;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getIssueQuantity() {
		return issueQuantity;
	}
	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}
	
	public String getUomType() {
		return uomType;
	}
	public void setUomType(String uomType) {
		this.uomType = uomType;
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
	public String getUomUnit() {
		return uomUnit;
	}
	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}
	public Integer getIndentQuantity() {
		return indentQuantity;
	}
	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}
	public List<BatchResponse> getBatch() {
		return batch;
	}
	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getCreditPayable() {
		return creditPayable;
	}
	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}
	public Double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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
	public Double getUnitDiscountAmount() {
		return unitDiscountAmount;
	}
	public void setUnitDiscountAmount(Double unitDiscountAmount) {
		this.unitDiscountAmount = unitDiscountAmount;
	}
	public Double getPackageConsumptionAmount() {
		return packageConsumptionAmount;
	}
	public void setPackageConsumptionAmount(Double packageConsumptionAmount) {
		this.packageConsumptionAmount = packageConsumptionAmount;
	}
	public Double getConcessionAmount() {
		return concessionAmount;
	}
	public void setConcessionAmount(Double concessionAmount) {
		this.concessionAmount = concessionAmount;
	}
	public Double getUnitConsessionAmount() {
		return unitConsessionAmount;
	}
	public void setUnitConsessionAmount(Double unitConsessionAmount) {
		this.unitConsessionAmount = unitConsessionAmount;
	}

	
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public Integer getParentIssueDetailId() {
		return parentIssueDetailId;
	}
	public void setParentIssueDetailId(Integer parentIssueDetailId) {
		this.parentIssueDetailId = parentIssueDetailId;
	}
	public Boolean getIsSubstitute() {
		return isSubstitute;
	}
	public void setIsSubstitute(Boolean isSubstitute) {
		this.isSubstitute = isSubstitute;
	}
	public Boolean getIsNarcoticSubstitute() {
		return isNarcoticSubstitute;
	}
	public void setIsNarcoticSubstitute(Boolean isNarcoticSubstitute) {
		this.isNarcoticSubstitute = isNarcoticSubstitute;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public List<BatchResponse> getNewBatch() {
		return newBatch;
	}
	public void setNewBatch(List<BatchResponse> newBatch) {
		this.newBatch = newBatch;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getAvailableStockQuantity() {
		return availableStockQuantity;
	}
	public void setAvailableStockQuantity(Integer availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}
	
	
}
