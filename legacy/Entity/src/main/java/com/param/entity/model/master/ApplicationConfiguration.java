package com.param.entity.model.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "ApplicationConfiguration")
@Table(name = "m_application_configuration", schema = "public")
public class ApplicationConfiguration extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(length = 10)
	private Boolean itemName;

	@Column(length = 100)
	private Integer defaultTaxId;

	@Column(length = 100)
	private Integer storeId;

	public Integer getStoreIp() {
		return storeIp;
	}

	public void setStoreIp(Integer storeIp) {
		this.storeIp = storeIp;
	}

	@Column(length = 100)
	private Integer storeIp;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	@Column(length = 10)
	private Boolean issueMaterialPickerRequired;

	@Column(length = 10)
	private Boolean taxInclusive;

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

	@Column(length = 10)
	private Boolean assignPickerRequired;

	@Column(length = 10)
	private Boolean isMarkup;

	@Column(length = 10)
	private Boolean markupUp;

	@Column(length = 10)
	private Boolean isDeptPharmacyRequired;

	@Column(name = "is_pharmvalidation_op_required", length = 10)
	protected Boolean isPharmValidationOPRequired;
	@Column(name = "is_assign_picker_op_required", length = 10)
	protected Boolean isAssignPickerOPRequired;
	@Column(name = "is_skip_pharmacy_settlement", length = 10)
	protected Boolean isSkipPharmacySettlement;

	@Column(name = "inventory_store_id", length = 10)
	protected Integer  inventoryStoreId;
	
	@Column(name = "is_skip_physical_inspection", length = 10)
	protected Boolean isSkipPhysicalInspection;
	
	@Column(name = "is_skip_po_approval", length = 10)
	protected Boolean isPoApproval;
	
	@Column(name = "auto_batch", length = 10)
	protected Boolean autoBatch;
	
	@Column(name = "auto_prescribed_qty", length = 10)
	protected Boolean autoPrescribedQty;
	
	@Column(name = "is_pharmacological_validation_skip_ip", length = 10)
	protected boolean isPharmacologicalValidationSkipIp;
	
	@Column(name = "op_stat_not_required", length = 10)
	protected boolean opStatNotRequired;
	
	@Column(name = "ip_stat_not_required", length = 10)
	protected boolean ipStatNotRequired;
	 
	@Column(name = "is_auto_consumable_acceptance", length = 10)
	protected Boolean isAutoConsumableAcceptance;
	
	@Column(name = "is_auto_medication_acceptance", length = 10)
	protected Boolean isAutoMedicationAcceptance;
	
    @Column(name = "cop_required", length = 10)
    protected Boolean atCopRequired;
    
    @Column(name = "is_manual_barcode_required" , length = 10)
    protected boolean isManualBarcodeRequired;
    
    @Column(name = "is_prn_required" , length = 10)
    protected boolean isPRNRequired;
    
    @Column(name = "virtual_store_id", length = 10)
	protected Integer  virtualStoreId;
	
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

	public Boolean getIsDeptPharmacyRequired() {
		return isDeptPharmacyRequired;
	}

	public void setIsDeptPharmacyRequired(Boolean isDeptPharmacyRequired) {
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
	}

	public Boolean getIsSkipPharmacySettlement() {
		return isSkipPharmacySettlement;
	}

	public void setIsSkipPharmacySettlement(Boolean isSkipPharmacySettlement) {
		this.isSkipPharmacySettlement = isSkipPharmacySettlement;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ApplicationConfiguration() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public ApplicationConfiguration(Unit unit, Boolean itemName, Integer defaultTaxId,
			Boolean issueMaterialPickerRequired, Boolean taxInclusive, Boolean assignPickerRequired) {
		super();
		this.unit = unit;
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
	}

	public ApplicationConfiguration(Boolean itemName, Integer defaultTaxId, Boolean issueMaterialPickerRequired,
			Boolean taxInclusive, Boolean assignPickerRequired, Boolean isMarkup, Boolean markupUp,

			Boolean isDeptPharmacyRequired, Boolean isPharmValidationOPRequired, Boolean isAssignPickerOPRequired,
			Integer storeId, Integer storeIp ,Integer inventoryStoreId) {
		super();
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
		this.isMarkup = isMarkup;
		this.markupUp = markupUp;
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
		this.isPharmValidationOPRequired = isPharmValidationOPRequired;
		this.isAssignPickerOPRequired = isAssignPickerOPRequired;
		this.storeId = storeId;
		this.storeIp = storeIp;
		this.inventoryStoreId=inventoryStoreId;
	}

	public void updateApplicationConfiguration(Boolean itemName, Integer defaultTaxId,
			Boolean issueMaterialPickerRequired, Boolean taxInclusive, Boolean assignPickerRequired, Unit unit) {
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
		this.unit = unit;
	}

	public void updateApplicationConfiguration(Boolean itemName, Integer defaultTaxId,
			Boolean issueMaterialPickerRequired, Boolean taxInclusive, Boolean assignPickerRequired, Boolean isMarkup,
			Boolean markupUp, Boolean isDeptPharmacyRequired) {
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
		this.isMarkup = isMarkup;
		this.markupUp = markupUp;
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
	}

	public void updateApplicationConfiguration(Boolean itemName, Integer defaultTaxId,
			Boolean issueMaterialPickerRequired, Boolean taxInclusive, Boolean assignPickerRequired, Boolean isMarkup,
			Boolean markupUp, Boolean isDeptPharmacyRequired, Boolean isPharmValidationOPRequired,

			Boolean isAssignPickerOPRequired, Integer storeId, Integer storeIp,Integer inventoryStoreId, boolean isPharmacologicalValidationSkipIp, 
			boolean opStatNotRequired, boolean ipStatNotRequired,boolean isManualBarcodeRequired,boolean isPRNRequired
			) {
		this.itemName = itemName;
		this.defaultTaxId = defaultTaxId;
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
		this.taxInclusive = taxInclusive;
		this.assignPickerRequired = assignPickerRequired;
		this.isMarkup = isMarkup;
		this.markupUp = markupUp;
		this.isDeptPharmacyRequired = isDeptPharmacyRequired;
		this.isPharmValidationOPRequired = isPharmValidationOPRequired;
		this.isAssignPickerOPRequired = isAssignPickerOPRequired;
		this.storeId = storeId;
		this.storeIp = storeIp;
		this.inventoryStoreId=inventoryStoreId;
		this.isPharmacologicalValidationSkipIp=isPharmacologicalValidationSkipIp;
		this.opStatNotRequired=opStatNotRequired;
		this.ipStatNotRequired=ipStatNotRequired;
		this.isManualBarcodeRequired=isManualBarcodeRequired;
		this.isPRNRequired=isPRNRequired;

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
