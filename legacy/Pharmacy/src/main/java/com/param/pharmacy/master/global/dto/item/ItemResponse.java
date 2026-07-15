package com.param.pharmacy.master.global.dto.item;

import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementDetailsResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public abstract class ItemResponse extends CookieObjectDto{

	protected Integer id;

	protected Integer genericId;

	protected String genericName;

	protected Integer formulationTypeId;

	protected String formulationType;

	protected Integer assetTypeId;

	protected String assetType;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer purchaseTaxId;

	protected Integer saleTaxId;

	protected String purchaseTax;

	protected String saleTax;

	protected Integer pharmalogicalClassificationId;

	protected String pharmalogicalClassification;

	protected Integer manufacturerId;

	protected String manufacturer;

	protected String hsnCode;

	protected Integer storageUnit;

	protected String storageUnitName;

	protected Integer strengthUnitId;

	protected String strengthUnit;

	protected Double strength;

	protected String code;

	protected String name;

	protected String productCode;

	protected Boolean status;

	protected Boolean isReusable;

	protected Boolean isVaccine;

	protected Boolean isOtc;

	protected Boolean isCombination;

	protected Boolean isBatchRequired;
	
	protected boolean isThreeUOMApplicable;

	protected Boolean isHighrisk;
	
	protected String cimsCode;
	protected String cimsType;

	protected Boolean isPsychotropic;
	protected Boolean isInjection;
	private Integer reusableCount; 
	private List<ReusablePercentage> reusablePercentage;
	private String reusablePercentage1;
	
	private boolean isDoseCalculationApplicable;
	private List<DoseCalculationApplicableDataDto> doseCalculationApplicableData1;

	private String doseCalculationApplicableData;
	
	protected List<UnitOfMeasurementListResponse> unitOfMeasurementDetailsResponse;
	
	private Boolean isEmergencyDrug;
	
	public Boolean getIsEmergencyDrug() {
		return isEmergencyDrug;
	}

	public void setIsEmergencyDrug(Boolean isEmergencyDrug) {
		this.isEmergencyDrug = isEmergencyDrug;
	}

	public String getReusablePercentage1() {
		return reusablePercentage1;
	}

	public void setReusablePercentage1(String reusablePercentage1) {
		this.reusablePercentage1 = reusablePercentage1;
	}

	public Integer getReusableCount() {
		return reusableCount;
	}

	public void setReusableCount(Integer reusableCount) {
		this.reusableCount = reusableCount;
	}

	public List<ReusablePercentage> getReusablePercentage() {
		return reusablePercentage;
	}

	public void setReusablePercentage(List<ReusablePercentage> reusablePercentage) {
		this.reusablePercentage = reusablePercentage;
	}

	public Boolean getIsInjection() {
		return isInjection;
	}

	public void setIsInjection(Boolean isInjection) {
		this.isInjection = isInjection;
	}

	protected Boolean isKit;

	protected Boolean isInfusion;

	protected Boolean isNarcotic;

	protected Boolean isConsumable;

	protected Boolean isImplant;

	protected Boolean isSterilize;

	protected Boolean isConsignment;

	protected Boolean isHospitalFormulary;
	protected Boolean isLasa;

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

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public String getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(String formulationType) {
		this.formulationType = formulationType;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public String getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(String strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getPurchaseTaxId() {
		return purchaseTaxId;
	}

	public void setPurchaseTaxId(Integer purchaseTaxId) {
		this.purchaseTaxId = purchaseTaxId;
	}

	public Integer getSaleTaxId() {
		return saleTaxId;
	}

	public void setSaleTaxId(Integer saleTaxId) {
		this.saleTaxId = saleTaxId;
	}

	public Integer getPharmalogicalClassificationId() {
		return pharmalogicalClassificationId;
	}

	public void setPharmalogicalClassificationId(Integer pharmalogicalClassificationId) {
		this.pharmalogicalClassificationId = pharmalogicalClassificationId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(Integer storageUnit) {
		this.storageUnit = storageUnit;
	}

	public String getPurchaseTax() {
		return purchaseTax;
	}

	public void setPurchaseTax(String purchaseTax) {
		this.purchaseTax = purchaseTax;
	}

	public String getSaleTax() {
		return saleTax;
	}

	public void setSaleTax(String saleTax) {
		this.saleTax = saleTax;
	}

	public String getPharmalogicalClassification() {
		return pharmalogicalClassification;
	}

	public void setPharmalogicalClassification(String pharmalogicalClassification) {
		this.pharmalogicalClassification = pharmalogicalClassification;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getStorageUnitName() {
		return storageUnitName;
	}

	public void setStorageUnitName(String storageUnitName) {
		this.storageUnitName = storageUnitName;
	}

	public Boolean getIsImplant() {
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

	public List<UnitOfMeasurementListResponse> getUnitOfMeasurementDetailsResponse() {
		return unitOfMeasurementDetailsResponse;
	}

	public void setUnitOfMeasurementDetailsResponse(
			List<UnitOfMeasurementListResponse> unitOfMeasurementDetailsResponse) {
		this.unitOfMeasurementDetailsResponse = unitOfMeasurementDetailsResponse;
	}

	public boolean getIsDoseCalculationApplicable() {
		return isDoseCalculationApplicable;
	}

	public void setIsDoseCalculationApplicable(boolean isDoseCalculationApplicable) {
		this.isDoseCalculationApplicable = isDoseCalculationApplicable;
	}

	public String getDoseCalculationApplicableData() {
		return doseCalculationApplicableData;
	}

	public void setDoseCalculationApplicableData(String doseCalculationApplicableData) {
		this.doseCalculationApplicableData = doseCalculationApplicableData;
	}

	public List<DoseCalculationApplicableDataDto> getDoseCalculationApplicableData1() {
		return doseCalculationApplicableData1;
	}

	public void setDoseCalculationApplicableData1(List<DoseCalculationApplicableDataDto> doseCalculationApplicableData1) {
		this.doseCalculationApplicableData1 = doseCalculationApplicableData1;
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
}
