package com.param.pharmacy.master.unit.dto.applicationConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class ApplicationConfigurationResponse extends CookieObjectDto{

	protected Integer id;

	protected Boolean status;

	protected Boolean itemName;

	protected Integer defaultTaxId;

	protected Boolean issueMaterialPickerRequired;

	protected Boolean taxInclusive;

	protected Boolean assignPickerRequired;

	protected Boolean isMarkup;
	protected Integer storeId;
	protected Integer storeIp;
	protected Integer inventoryStoreId;
	protected Boolean isSkipPhysicalInspection;
	protected Boolean isPoApproval;
	protected Boolean autoBatch;
	protected Boolean autoPrescribedQty;
	protected boolean isPharmacologicalValidationSkipIp;
	protected Boolean isAutoConsumableAcceptance;
	protected Boolean isAutoMedicationAcceptance;
    protected Boolean atCopRequired;
	protected boolean opStatNotRequired;
	protected boolean ipStatNotRequired;
	protected boolean isManualBarcodeRequired;
	protected boolean isPRNRequired;
	protected Integer virtualStoreId;
	public Boolean getIsAutoConsumableAcceptance() {
		return isAutoConsumableAcceptance;
	}

	public void setIsAutoConsumableAcceptance(Boolean isAutoConsumableAcceptance) {
		this.isAutoConsumableAcceptance = isAutoConsumableAcceptance;
	}

	public Boolean getIsAutoMedicationAcceptance() {
		return isAutoMedicationAcceptance;
	}

	public void setIsAutoMedicationAcceptance(Boolean isAutoMedicationAcceptance) {
		this.isAutoMedicationAcceptance = isAutoMedicationAcceptance;
	}
	
    public Boolean getatCopRequired() {
        return atCopRequired;
    }

    public void setatCopRequired(Boolean atCopRequired) {
        this.atCopRequired = atCopRequired;
    }

	public Boolean getIsPoApproval() {
		return isPoApproval;
	}

	public void setIsPoApproval(Boolean isPoApproval) {
		this.isPoApproval = isPoApproval;
	}

	public Boolean getIsSkipPhysicalInspection() {
		return isSkipPhysicalInspection;
	}

	public void setIsSkipPhysicalInspection(Boolean isSkipPhysicalInspection) {
		this.isSkipPhysicalInspection = isSkipPhysicalInspection;
	}

	public Integer getStoreIp() {
		return storeIp;
	}

	public void setStoreIp(Integer storeIp) {
		this.storeIp = storeIp;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	protected Boolean markupUp;
	protected Boolean isDeptPharmacyRequired;
	protected Boolean isPharmValidationOPRequired;
	protected Boolean isSkipPharmacySettlement;
	public Boolean getIsSkipPharmacySettlement() {
		return isSkipPharmacySettlement;
	}

	public void setIsSkipPharmacySettlement(Boolean isSkipPharmacySettlement) {
		this.isSkipPharmacySettlement = isSkipPharmacySettlement;
	}

	public Boolean getIsPharmValidationOPRequired() {
		return isPharmValidationOPRequired;
	}

	public void setIsPharmValidationOPRequired(Boolean isPharmValidationOPRequired) {
		this.isPharmValidationOPRequired = isPharmValidationOPRequired;
	}

	public Boolean getIsAssignPickerOPRequired() {
		return isAssignPickerOPRequired;
	}

	public void setIsAssignPickerOPRequired(Boolean isAssignPickerOPRequired) {
		this.isAssignPickerOPRequired = isAssignPickerOPRequired;
	}

	protected Boolean isAssignPickerOPRequired;

	public Boolean getIsDeptPharmacyRequired() {
		return isDeptPharmacyRequired;
	}

	public void setIsDeptPharmacyRequired(Boolean isDeptPharmacyRequired) {
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
	}

	protected List<ApplicationConfigurationResponse> items;

	public ApplicationConfigurationResponse() {
		super();
	}

	public ApplicationConfigurationResponse(Integer id, Boolean status, Boolean itemName, Integer defaultTaxId,
			Boolean issueMaterialPickerRequired, Boolean taxInclusive, Boolean assignPickerRequired, Boolean isMarkup,
			Boolean markupUp , Boolean isDeptPharmacyRequired , Boolean isPharmValidationOPRequired,  Boolean isAssignPickerOPRequired ,Integer storeId , Integer storeIp,Integer inventoryStoreId, 
			boolean isPharmacologicalValidationSkipIp, boolean opStatNotRequired, boolean ipStatNotRequired,boolean isManualBarcodeRequired,boolean isPRNRequired) {
		super();
		this.id = id;
		this.status = status;
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
		this.isMarkup = isMarkup;
		this.markupUp = markupUp;
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;

		this.isPharmValidationOPRequired =isPharmValidationOPRequired;
		this.isAssignPickerOPRequired=isAssignPickerOPRequired;
		this.storeId = storeId;
		this.storeIp = storeIp;
		this.inventoryStoreId=inventoryStoreId;
		this.isPharmacologicalValidationSkipIp=isPharmacologicalValidationSkipIp;
		this.opStatNotRequired=opStatNotRequired;
		this.ipStatNotRequired=ipStatNotRequired;
		this.isManualBarcodeRequired=isManualBarcodeRequired;
		this.isPRNRequired=isPRNRequired;
	}

	public ApplicationConfigurationResponse(Integer id, Boolean status, Boolean taxInclusive,
			Boolean assignPickerRequired) {
		super();
		this.id = id;
		this.status = status;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
	}

	public List<ApplicationConfigurationResponse> getItems() {
		return items;
	}

	public void setItems(List<ApplicationConfigurationResponse> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getItemName() {
		return itemName;
	}

	public void setItemName(Boolean itemName) {
		this.itemName = itemName;
	}

	public Integer getDefaultTaxId() {
		return defaultTaxId;
	}

	public void setDefaultTaxId(Integer defaultTaxId) {
		this.defaultTaxId = defaultTaxId;
	}

	public Boolean getIssueMaterialPickerRequired() {
		return issueMaterialPickerRequired;
	}

	public void setIssueMaterialPickerRequired(Boolean issueMaterialPickerRequired) {
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
	}

	public String getIsActive() {
		return null;
	}

	public void addItems(ApplicationConfigurationResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ApplicationConfigurationResponse>();

		this.items.add(item);
	}

	public Boolean getTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(Boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	public Boolean getAssignPickerRequired() {
		return assignPickerRequired;
	}

	public void setAssignPickerRequired(Boolean assignPickerRequired) {
		this.assignPickerRequired = assignPickerRequired;
	}

	public Boolean getIsMarkup() {
		return isMarkup;
	}

	public void setIsMarkup(Boolean isMarkup) {
		this.isMarkup = isMarkup;
	}

	public Boolean getMarkupUp() {
		return markupUp;
	}

	public void setMarkupUp(Boolean markupUp) {
		this.markupUp = markupUp;
	}

	public Integer getInventoryStoreId() {
		return inventoryStoreId;
	}

	public void setInventoryStoreId(Integer inventoryStoreId) {
		this.inventoryStoreId = inventoryStoreId;
	}

	public Boolean getAutoBatch() {
		return autoBatch;
	}

	public void setAutoBatch(Boolean autoBatch) {
		this.autoBatch = autoBatch;
	}

	public Boolean getAutoPrescribedQty() {
		return autoPrescribedQty;
	}

	public void setAutoPrescribedQty(Boolean autoPrescribedQty) {
		this.autoPrescribedQty = autoPrescribedQty;
	}

	public boolean getIsPharmacologicalValidationSkipIp() {
		return isPharmacologicalValidationSkipIp;
	}

	public void setIsPharmacologicalValidationSkipIp(boolean isPharmacologicalValidationSkipIp) {
		this.isPharmacologicalValidationSkipIp = isPharmacologicalValidationSkipIp;
	}

	public boolean getOpStatNotRequired() {
		return opStatNotRequired;
	}

	public void setOpStatNotRequired(boolean opStatNotRequired) {
		this.opStatNotRequired = opStatNotRequired;
	}

	public boolean getIpStatNotRequired() {
		return ipStatNotRequired;
	}

	public void setIpStatNotRequired(boolean ipStatNotRequired) {
		this.ipStatNotRequired = ipStatNotRequired;
	}

	public boolean getIsManualBarcodeRequired() {
		return isManualBarcodeRequired;
	}

	public void setIsManualBarcodeRequired(boolean isManualBarcodeRequired) {
		this.isManualBarcodeRequired = isManualBarcodeRequired;
	}

	public boolean getIsPRNRequired() {
		return isPRNRequired;
	}

	public void setIsPRNRequired(boolean isPRNRequired) {
		this.isPRNRequired = isPRNRequired;
	}

	public Integer getVirtualStoreId() {
		return virtualStoreId;
	}

	public void setVirtualStoreId(Integer virtualStoreId) {
		this.virtualStoreId = virtualStoreId;
	}

	

}
