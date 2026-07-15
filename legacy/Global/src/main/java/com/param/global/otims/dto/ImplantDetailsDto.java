package com.param.global.otims.dto;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class ImplantDetailsDto  extends CommonBillingItemsDto{

	private Integer implantDetailsId;
	
	private Integer kitId;
	
	private Integer qty;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String kitName;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String itemName;
	
	private String fromDate;
	
	private String isItemOrKit;
	
	private String toDate;
	
	private Integer wardId;
	
	private Boolean isActive;
	
	private Integer procedureOrderId;
	
	private Integer itemQuantity;
	
	private String kitItemName;
	
	private Integer kitItemId;

	private Boolean isConsumable;
	
	private String itemCode;
	
	private String kitCode;
	
	private Integer kitTypeId;
	
	private Boolean isConsignment;
	
	private Boolean isImplant;
	
	private Boolean isKit;
	
	private Boolean isExported;
	
	private String kitItemCode;
	private Integer itemTaxId;
	private double itemTaxPercent;
	private Integer kitTaxId;
	private double kitTaxPercent;
	private Integer itemProductCategoryId;
	private Integer itemAssetCategoryId;
	private Integer kitAssetCategoryId;
	private Integer kitProductCategoryId;
	private String itemTaxName;
	private String kitTaxName;
	
	private Boolean isExtra;

	public Integer getImplantDetailsId() {
		return implantDetailsId;
	}

	public void setImplantDetailsId(Integer implantDetailsId) {
		this.implantDetailsId = implantDetailsId;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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

	public Integer getKitItemId() {
		return kitItemId;
	}

	public void setKitItemId(Integer kitItemId) {
		this.kitItemId = kitItemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getKitCode() {
		return kitCode;
	}

	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}

	public Integer getKitTypeId() {
		return kitTypeId;
	}

	public void setKitTypeId(Integer kitTypeId) {
		this.kitTypeId = kitTypeId;
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

	public String getKitItemCode() {
		return kitItemCode;
	}

	public void setKitItemCode(String kitItemCode) {
		this.kitItemCode = kitItemCode;
	}

	public Integer getItemTaxId() {
		return itemTaxId;
	}

	public void setItemTaxId(Integer itemTaxId) {
		this.itemTaxId = itemTaxId;
	}

	public double getItemTaxPercent() {
		return itemTaxPercent;
	}

	public void setItemTaxPercent(double itemTaxPercent) {
		this.itemTaxPercent = itemTaxPercent;
	}

	public Integer getKitTaxId() {
		return kitTaxId;
	}

	public void setKitTaxId(Integer kitTaxId) {
		this.kitTaxId = kitTaxId;
	}

	public double getKitTaxPercent() {
		return kitTaxPercent;
	}

	public void setKitTaxPercent(double kitTaxPercent) {
		this.kitTaxPercent = kitTaxPercent;
	}

	public Integer getItemProductCategoryId() {
		return itemProductCategoryId;
	}

	public void setItemProductCategoryId(Integer itemProductCategoryId) {
		this.itemProductCategoryId = itemProductCategoryId;
	}

	public Integer getItemAssetCategoryId() {
		return itemAssetCategoryId;
	}

	public void setItemAssetCategoryId(Integer itemAssetCategoryId) {
		this.itemAssetCategoryId = itemAssetCategoryId;
	}

	public Integer getKitAssetCategoryId() {
		return kitAssetCategoryId;
	}

	public void setKitAssetCategoryId(Integer kitAssetCategoryId) {
		this.kitAssetCategoryId = kitAssetCategoryId;
	}

	public Integer getKitProductCategoryId() {
		return kitProductCategoryId;
	}

	public void setKitProductCategoryId(Integer kitProductCategoryId) {
		this.kitProductCategoryId = kitProductCategoryId;
	}

	public String getItemTaxName() {
		return itemTaxName;
	}

	public void setItemTaxName(String itemTaxName) {
		this.itemTaxName = itemTaxName;
	}

	public String getKitTaxName() {
		return kitTaxName;
	}

	public void setKitTaxName(String kitTaxName) {
		this.kitTaxName = kitTaxName;
	}

	public Boolean getIsExtra() {
		return isExtra;
	}

	public void setIsExtra(Boolean isExtra) {
		this.isExtra = isExtra;
	}
	
}
