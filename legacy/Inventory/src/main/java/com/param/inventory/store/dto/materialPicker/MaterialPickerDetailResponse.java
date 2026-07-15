package com.param.inventory.store.dto.materialPicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.store.dto.materialIssuePending.IssueBatch;

@JsonInclude(Include.NON_NULL)
public class MaterialPickerDetailResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected Integer uomTypeId;

	protected String uomType;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected Integer indentQuantity;

	protected Integer pendingQuantity;

	protected Integer pickerQuantity;

	protected Long issueQuantity;

	protected Long stockAvailable;

	protected Long batchId;

	protected String batchNo;

	protected Date expiry;

	protected List<BatchResponse> batch;
	protected Integer storeIndentDetailId;

	protected List<IssueBatch> issueBatch;
	protected Integer genericId;
	protected List<SubstituteMaterialIssueDetailDto> selectedItems;
	protected BatchIssueResponse batchObj;
	protected Boolean isBatchRequired;
	protected Integer indentAppprovedQuantity;
	protected Integer materialIssueId;
	protected Integer parentMaterialIssueId;
	protected String doseUnit;
	protected Double dose;
	protected Integer formulationTypeId;
	
	private boolean isThreeUOMApplicable;
    protected List<UomResponse> uom;

    public List<UomResponse> getUom() {
        return uom;
    }

    public void setUom(List<UomResponse> uom) {
        this.uom = uom;
    }


    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }


	public Integer getIndentAppprovedQuantity() {
		return indentAppprovedQuantity;
	}

	public void setIndentAppprovedQuantity(Integer indentAppprovedQuantity) {
		this.indentAppprovedQuantity = indentAppprovedQuantity;
	}

	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public MaterialPickerDetailResponse() {
		super();
	}

	public MaterialPickerDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			String genericName, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,
			Long stockAvailable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.stockAvailable = stockAvailable;
	}

	public MaterialPickerDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			String genericName, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,
			Integer indentQuantity, Integer pendingQuantity, Long stockAvailable) {
		this(id, itemId, itemCode, itemName, genericName, uomTypeId, uomType, uomUnitId, uomUnit, stockAvailable);
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
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

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public void addBatches(BatchResponse batch) {
		if (this.batch == null)
			this.batch = new ArrayList<BatchResponse>();

		this.batch.add(batch);
	}

	public BatchIssueResponse getBatchObj() {
		return batchObj;
	}

	public void setBatchObj(BatchIssueResponse batchObj) {
		this.batchObj = batchObj;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getPickerQuantity() {
		return pickerQuantity;
	}

	public void setPickerQuantity(Integer pickerQuantity) {
		this.pickerQuantity = pickerQuantity;
	}

	public Long getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Long issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public List<IssueBatch> getBatches() {
		return issueBatch;
	}

	public void setBatches(List<IssueBatch> issueBatch) {
		this.issueBatch = issueBatch;
	}

	public void addBatch(IssueBatch batch) {
		if (this.issueBatch == null)
			this.issueBatch = new ArrayList<IssueBatch>();

		this.issueBatch.add(batch);
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public List<SubstituteMaterialIssueDetailDto> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<SubstituteMaterialIssueDetailDto> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Integer getMaterialIssueId() {
		return materialIssueId;
	}

	public void setMaterialIssueId(Integer materialIssueId) {
		this.materialIssueId = materialIssueId;
	}

	public Integer getParentMaterialIssueId() {
		return parentMaterialIssueId;
	}

	public void setParentMaterialIssueId(Integer parentMaterialIssueId) {
		this.parentMaterialIssueId = parentMaterialIssueId;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Integer getStoreIndentDetailId() {
		return storeIndentDetailId;
	}

	public void setStoreIndentDetailId(Integer storeIndentDetailId) {
		this.storeIndentDetailId = storeIndentDetailId;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}
	
	
}