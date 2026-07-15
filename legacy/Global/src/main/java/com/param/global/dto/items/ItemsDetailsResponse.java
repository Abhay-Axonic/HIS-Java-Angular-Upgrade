package com.param.global.dto.items;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemsDetailsResponse extends ItemsDetail {

	protected List<BatchResponse> batch = new ArrayList<BatchResponse>();

	protected Long allocationId;

	protected String message;

	protected Integer currentStock;
	
	protected Integer productCategoryId;
	
	protected Integer markUpDown;
	
	protected Double markUpDownPer;
	
	protected Double patientClassPercentage;
	
	protected boolean markupStatus;
	
	protected Integer priorityId;
	protected boolean isSellable;
	protected Integer minQty;
	protected Integer maxQty;
	protected String colorCode;
    private boolean isThreeUOMApplicable;
    private boolean isNarcotic;
    private boolean isOTC;
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
	
	protected String storageUnitName;
 	public String getStorageUnitName() {
		return storageUnitName;
	}

	public void setStorageUnitName(String storageUnitName) {
		this.storageUnitName = storageUnitName;
	}

	public boolean isSellable() {
		return isSellable;
	}

	public void setSellable(boolean isSellable) {
		this.isSellable = isSellable;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public boolean isMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public ItemsDetailsResponse() {
		super();
	}

	public ItemsDetailsResponse(Integer itemId, String itemCode, String itemName, Integer genericId, String genericName,
			Integer manufacturerId, String manufacturerName, Integer taxId, String taxName, Double taxPercent,
			Boolean isOtc,Long batchId,String prePrintedBarcode, List<BatchResponse> batch) {
		super(itemId, itemCode, itemName, genericId, genericName, manufacturerId, manufacturerName, taxId, taxName,
				taxPercent, isOtc,batchId,prePrintedBarcode);
		this.batch = batch;
	}

	public Long getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public void addBatch(BatchResponse batch) {
		this.batch.add(batch);
	}

	public void removeBatch(BatchResponse batch) {
		this.batch.remove(batch);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public boolean getIsOTC() {
		return isOTC;
	}

	public void setIsOTC(boolean isOTC) {
		this.isOTC = isOTC;
	}
	
}
