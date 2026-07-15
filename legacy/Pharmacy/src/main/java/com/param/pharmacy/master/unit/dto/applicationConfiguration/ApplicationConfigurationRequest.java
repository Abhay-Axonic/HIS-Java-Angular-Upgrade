package com.param.pharmacy.master.unit.dto.applicationConfiguration;

@SuppressWarnings("serial")
public class ApplicationConfigurationRequest extends ApplicationConfigurationResponse {

	protected Integer unitId;
	protected Boolean itemName;
	protected Integer defaultTaxId;
	protected Boolean issueMaterialPickerRequired;
	protected Boolean taxInclusive;
	protected Integer storeOp;
	protected Integer storeIp;
	protected Integer  inventoryStoreId;
	protected Boolean isSkipPhysicalInspection;
	protected Boolean isPoApproval;
	protected Boolean autoBatch;
	protected Boolean autoPrescribedQty;
	protected boolean isPharmacologicalValidationSkipIp;
	protected Boolean isAutoMedicationAcceptance;
    protected Boolean atCopRequired;
	protected Boolean isAutoConsumableAcceptance;
	protected boolean opStatNotRequired;
	protected boolean ipStatNotRequired;
	protected boolean isManualBarcodeRequired;
	protected boolean isPRNRequired;
	protected Integer virtualStoreId;
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

	public Boolean getIsAutoConsumableAcceptance() {
		return isAutoConsumableAcceptance;
	}

	public void setIsAutoConsumableAcceptance(Boolean isAutoConsumableAcceptance) {
		this.isAutoConsumableAcceptance = isAutoConsumableAcceptance;
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

	public Integer getStoreOp() {
		return storeOp;
	}

	public void setStoreOp(Integer storeOp) {
		this.storeOp = storeOp;
	}

	protected Boolean assignPickerRequired;
	
	protected Boolean isPharmValidationOPRequired;
	protected Boolean isAssignPickerOPRequired;
	protected Boolean isSkipPharmacySettlement;

	protected Boolean isMarkup;

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

	protected Boolean markupUp;
	protected Boolean isDeptPharmacyRequired;

	public Boolean getIsDeptPharmacyRequired() {
		return isDeptPharmacyRequired;
	}

	public void setIsDeptPharmacyRequired(Boolean isDeptPharmacyRequired) {
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
	}

	public ApplicationConfigurationRequest() {
		super();
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
