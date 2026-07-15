package com.param.pharmacy.master.global.dto.item;

import java.util.List;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.GetStoreItemResponse;

public class ItemRequest extends ItemResponse {

	protected Integer manufacturerId;

	protected Integer pharmacologicalClassificationId;

	protected Integer strengthUnitId;

	protected String hsnCode;

	protected Integer storageUnitId;

	protected double strength;

	protected Integer sTaxId;

	protected Integer pTaxId;

	protected Boolean isReusable;

	protected Boolean isVaccine;

	protected Boolean isOtc;

	protected Boolean isCombination;

	protected Boolean isBatchRequired;
	
	protected boolean isThreeUOMApplicable;

	protected Boolean isHighrisk;

	protected Boolean isPsychotropic;

	protected Boolean isKit;

	protected Boolean isInfusion;

	protected Boolean isNarcotic;

	protected Boolean isConsumable;

	protected Boolean isConsignment;

	protected Boolean isHospitalFormulary;

	protected Integer itemRequestId;
	
	protected Integer quantity;
	protected Boolean  isInjection;

	//protected Boolean isImplant;

//	protected Boolean isSterilize;
	protected Integer productCategoryId;
	
	protected MultipartFile file;
	
	private Integer successCount;
	
	private Integer failureCount;
	
	private String failedRemark;
	
	protected UnitOfMeasurementRequest unitOfMeasurementRequest;
	
	protected List<GetStoreItemResponse> storeIds;
	
	protected String cimsCode;
	 
	protected String cimsType;
	
	protected Boolean isEmergencyDrug;
	protected Boolean isLasa;

	public Boolean getIsEmergencyDrug() {
		return isEmergencyDrug;
	}

	public void setIsEmergencyDrug(Boolean isEmergencyDrug) {
		this.isEmergencyDrug = isEmergencyDrug;
	}

	public List<GetStoreItemResponse> getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(List<GetStoreItemResponse> storeIds) {
		this.storeIds = storeIds;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Boolean getIsInjection() {
		return isInjection;
	}

	public void setIsInjection(Boolean isInjection) {
		this.isInjection = isInjection;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getItemRequestId() {
		return itemRequestId;
	}

	public void setItemRequestId(Integer itemRequestId) {
		this.itemRequestId = itemRequestId;
	}

	public ItemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getPharmacologicalClassificationId() {
		return pharmacologicalClassificationId;
	}

	public void setPharmacologicalClassificationId(Integer pharmacologicalClassificationId) {
		this.pharmacologicalClassificationId = pharmacologicalClassificationId;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public Integer getsTaxId() {
		return sTaxId;
	}

	public void setsTaxId(Integer sTaxId) {
		this.sTaxId = sTaxId;
	}

	public Integer getpTaxId() {
		return pTaxId;
	}

	public void setpTaxId(Integer pTaxId) {
		this.pTaxId = pTaxId;
	}

	public Boolean getIsReusable() {
		return isReusable;
	}

	public void setIsReusable(Boolean isReusable) {
		this.isReusable = isReusable;
	}

	public Boolean getIsVaccine() {
		return isVaccine;
	}

	public void setIsVaccine(Boolean isVaccine) {
		this.isVaccine = isVaccine;
	}

	public Boolean getIsOtc() {
		return isOtc;
	}

	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Boolean getIsCombination() {
		return isCombination;
	}

	public void setIsCombination(Boolean isCombination) {
		this.isCombination = isCombination;
	}

	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public Boolean getIsHighrisk() {
		return isHighrisk;
	}

	public void setIsHighrisk(Boolean isHighrisk) {
		this.isHighrisk = isHighrisk;
	}

	public Boolean getIsPsychotropic() {
		return isPsychotropic;
	}

	public void setIsPsychotropic(Boolean isPsychotropic) {
		this.isPsychotropic = isPsychotropic;
	}

	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public Boolean getIsInfusion() {
		return isInfusion;
	}

	public void setIsInfusion(Boolean isInfusion) {
		this.isInfusion = isInfusion;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsHospitalFormulary() {
		return isHospitalFormulary;
	}

	public void setIsHospitalFormulary(Boolean isHospitalFormulary) {
		this.isHospitalFormulary = isHospitalFormulary;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}

	public String getFailedRemark() {
		return failedRemark;
	}

	public void setFailedRemark(String failedRemark) {
		this.failedRemark = failedRemark;
	}

	public UnitOfMeasurementRequest getUnitOfMeasurementRequest() {
		return unitOfMeasurementRequest;
	}

	public void setUnitOfMeasurementRequest(UnitOfMeasurementRequest unitOfMeasurementRequest) {
		this.unitOfMeasurementRequest = unitOfMeasurementRequest;
	}

	public String getCimsCode() {
		return cimsCode;
	}

	public void setCimsCode(String cimsCode) {
		this.cimsCode = cimsCode;
	}

	public String getCimsType() {
		return cimsType;
	}

	public void setCimsType(String cimsType) {
		this.cimsType = cimsType;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
/*public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsSterilize() {
		return isSterilize;
	}

	public void setIsSterilize(Boolean isSterilize) {
		this.isSterilize = isSterilize;
	}
*/
	
	
}
