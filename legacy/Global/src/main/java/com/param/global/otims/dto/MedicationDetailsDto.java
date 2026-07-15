package com.param.global.otims.dto;

import java.util.List;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class MedicationDetailsDto extends CommonBillingItemsDto{

	private Integer medicationDetailsId;
	
	private Integer kitId;
	
	private Integer qty;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String kitName;
	
	private String isItemOrKit;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String itemName;
	
	private String itemCode;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer wardId;
	
	private Boolean isActive;
	
	private Boolean isConsumable;
	
	private Integer procedureOrderId;
	
	private Integer itemQuantity;
	
	private String kitItemName;
	
	private Integer kitItemId;
	
	private Boolean isConsignment;
	
	private Boolean isImplant;
	
	private Boolean isKit;
	
	private Boolean isExported;
	
	private Integer manufacturerId;
	
	private Integer assetCategoryId;
	
	private List<Integer> selectMedicationDetailsList;
	
	protected List<BatchDto> batch;

	private String taxName;
	
	private String manufacturerName;
	
	private Integer taxId; 
	
	private double taxPercent;
	
	private String genericName;
	
	private Integer productCategoryId; 
	
	private Boolean isExtra;
	
	public Integer getMedicationDetailsId() {
		return medicationDetailsId;
	}

	public void setMedicationDetailsId(Integer medicationDetailsId) {
		this.medicationDetailsId = medicationDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
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

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public List<Integer> getSelectMedicationDetailsList() {
		return selectMedicationDetailsList;
	}

	public void setSelectMedicationDetailsList(List<Integer> selectMedicationDetailsList) {
		this.selectMedicationDetailsList = selectMedicationDetailsList;
	}

	public Integer getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getKitItemName() {
		return kitItemName;
	}

	public void setKitItemName(String kitItemName) {
		this.kitItemName = kitItemName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getKitItemId() {
		return kitItemId;
	}

	public void setKitItemId(Integer kitItemId) {
		this.kitItemId = kitItemId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public Boolean getIsExported() {
		return isExported;
	}

	public void setIsExported(Boolean isExported) {
		this.isExported = isExported;
	}

	public String getIsItemOrKit() {
		return isItemOrKit;
	}

	public void setIsItemOrKit(String isItemOrKit) {
		this.isItemOrKit = isItemOrKit;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public List<BatchDto> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchDto> batch) {
		this.batch = batch;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Boolean getIsExtra() {
		return isExtra;
	}

	public void setIsExtra(Boolean isExtra) {
		this.isExtra = isExtra;
	}

	
}