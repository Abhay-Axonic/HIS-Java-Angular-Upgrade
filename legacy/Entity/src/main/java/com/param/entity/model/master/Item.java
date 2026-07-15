package com.param.entity.model.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.opd.MedicationHistory;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.model.VaccinationMaster;
import com.param.opd.coversheet.model.InfusionSolutionMaster;

@Entity(name = "Item")
@Table(name = "m_item", schema = "public")
public class Item extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(nullable = false, length = 10)
	private String code;

	@Column(name = "hsn_code", length = 50)
	private String hsnCode;

	@Column(name = "is_batch_required", nullable = false)
	private Boolean isBatchRequired = false;
	
	@Column(name = "is_three_uom_applicable", nullable = false)
	private boolean isThreeUOMApplicable = false;

	@Column(name = "is_combination", nullable = false)
	private Boolean isCombination = false;

	@Column(name = "is_high_risk_product", nullable = false)
	private Boolean isHighRiskProduct = false;

	@Column(name = "is_infusion", nullable = false)
	private Boolean isInfusion = false;

	@Column(name = "is_kit", nullable = false)
	private Boolean isKit = false;

	@Column(name = "is_narcotic", nullable = false)
	private Boolean isNarcotic = false;

	@Column(name = "is_otc", nullable = false)
	private Boolean isOtc = false;

	@Column(name = "is_psychotropic", nullable = false)
	private Boolean isPsychotropic = false;

	@Column(name = "is_reusable", nullable = false)
	private Boolean isReusable = false;

	@Column(name = "is_vaccin", nullable = false)
	private Boolean isVaccin = false;

	@Column(name = "is_consignment", nullable = false)
	private Boolean isConsignment = false;

	@Column(name = "is_consumable", nullable = false)
	private Boolean isConsumable = false;

	@Column(name = "is_implant", nullable = false)
	private Boolean isImplant = false;

	@Column(name = "is_sterilize", nullable = false)
	private Boolean isSterilize = false;

	@Column(name = "is_hospital_formulary", nullable = false)
	private Boolean isHospitalFormulary = false;

	@Column(name = "is_cssd", nullable = true)
	private Boolean isCssd = false;

	@Column(name = "is_modality", nullable = true)
	private Boolean isModality = false;

	@Column(name = "is_chargeable_equpiment", nullable = true)
	private Boolean isChargeableEqupiment = false;
	
	@Column(name = "is_dialyser", nullable = false)
	private Boolean isDialyser = false;
	
	@Column(name = "cims_code", length = 50)
	private String cimsCode;
	
	@Column(name = "cims_type", length = 50)
	private String cimsType;
	
	@Column(name = "is_critical", length = 50)
	private Boolean isCritical;
	
	@Column(name = "is_emergency_drug", length = 50)
	private Boolean isEmergencyDrug;

	@Column(name = "is_lasa", length = 50)
	private Boolean isLasa;


	public Boolean getIsEmergencyDrug() {
		return isEmergencyDrug;
	}

	public void setIsEmergencyDrug(Boolean isEmergencyDrug) {
		this.isEmergencyDrug = isEmergencyDrug;
	}

	public Boolean getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Boolean isCritical) {
		this.isCritical = isCritical;
	}

	public Boolean getIsDialyser() {
		return isDialyser;
	}

	public void setIsDialyser(Boolean isDialyser) {
		this.isDialyser = isDialyser;
	}

	public Boolean getIsInjection() {
		return isInjection;
	}

	public void setIsInjection(Boolean isInjection) {
		this.isInjection = isInjection;
	}

	@Column(name = "is_accessories", nullable = true)
	private Boolean isAccessories = false;
	@Column(name = "is_injection", nullable = true)
	private Boolean isInjection = false;
	 

	public Boolean getIsAccessories() {
		return isAccessories;
	}

	public void setIsAccessories(Boolean isAccessories) {
		this.isAccessories = isAccessories;
	}

	public Boolean getIsSpares() {
		return isSpares;
	}

	public void setIsSpares(Boolean isSpares) {
		this.isSpares = isSpares;
	}

	@Column(name = "is_spares", nullable = true)
	private Boolean isSpares = false;

	@Column(nullable = false, length = 500)
	private String name;

	@Column(name = "product_code", length = 50)
	private String productCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storage_unit_id")
	private StorageUnit storageUnit;

	@Column(name = "strength")
	private Double strength;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formulation_type_id")
	private FormulationType formulationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "generic_id")
	private Generic generic;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manufacturer_id", nullable = false)
	private Manufacturer manufacturer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", nullable = true)
	private ProductCategory productCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "strength_unit_id")
	private StrengthUnit strengthUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pharmacological_classification_id")
	private PharmacologicalClassification pharmacologicalClassification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_tax_id")
	private Tax purchaseTax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "s_tax_id")
	private Tax salesTax;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
	private List<UnitOfMeasurement> unitOfMeasurementList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
	private List<Batch> batchList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
	private List<StoreItemMapper> storeItemList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
	private List<StoreIndentDetail> storeIndentDetailList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemRequestDetail> itemRequestDetailList;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<VaccinationMaster> vaccinationMasterList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemMaster")
	private List<MedicationHistory> medicationHistoryList;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade=CascadeType.ALL)
	private List<ImplantSpeciality> implantSpecialityList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemMaster", cascade=CascadeType.ALL)
	private List<InfusionSolutionMaster> listInfusionSolutionMaster ;

	@Column(name = "reusable_count")
	private Integer reusableCount;
	
	@Column(name = "reusable_percentage")
	private String reusablePercentage;
	
	@Column(name = "is_dose_calculation_applicable")
	private boolean isDoseCalculationApplicable;
	
	@Column(name = "dose_calculation_applicable_data")
	private String doseCalculationApplicableData;
	
	public Integer getReusableCount() {
		return reusableCount;
	}

	public void setReusableCount(Integer reusableCount) {
		this.reusableCount = reusableCount;
	}

	public String getReusablePercentage() {
		return reusablePercentage;
	}

	public void setReusablePercentage(String reusablePercentage) {
		this.reusablePercentage = reusablePercentage;
	}

	public Item()
	{
		super();
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Item(Integer id) {
		super();
		this.id = id;
	}

	public Item(ProductCategory productCategory, String code, String productCode, String name,
			Manufacturer manufacturer,Tax purchaseTax, Tax salesTax,StorageUnit storageUnit, Boolean isConsignment, Boolean isSterilize)
	{
		super();
		this.productCategory=productCategory;
		this.code=code;
		this.productCode=productCode;
		this.name=name;
		this.manufacturer=manufacturer;
		this.purchaseTax=purchaseTax;
		this.salesTax=salesTax;
		this.storageUnit=storageUnit;
		this.isConsignment=isConsignment;
		this.isSterilize=isSterilize;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHsnCode() {
		return this.hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Boolean getIsBatchRequired() {
		return this.isBatchRequired;
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

	public Boolean getIsCombination() {
		return this.isCombination;
	}

	public void setIsCombination(Boolean isCombination) {
		this.isCombination = isCombination;
	}

	public Boolean getIsHighRiskProduct() {
		return this.isHighRiskProduct;
	}

	public void setIsHighRiskProduct(Boolean isHighRiskProduct) {
		this.isHighRiskProduct = isHighRiskProduct;
	}

	public Boolean getIsInfusion() {
		return this.isInfusion;
	}

	public void setIsInfusion(Boolean isInfusion) {
		this.isInfusion = isInfusion;
	}

	public Boolean getIsKit() {
		return this.isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public Boolean getIsNarcotic() {
		return this.isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public Boolean getIsOtc() {
		return this.isOtc;
	}

	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Boolean getIsPsychotropic() {
		return this.isPsychotropic;
	}

	public void setIsPsychotropic(Boolean isPsychotropic) {
		this.isPsychotropic = isPsychotropic;
	}

	public Boolean getIsReusable() {
		return this.isReusable;
	}

	public void setIsReusable(Boolean isReusable) {
		this.isReusable = isReusable;
	}

	public Boolean getIsVaccin() {
		return this.isVaccin;
	}

	public void setIsVaccin(Boolean isVaccin) {
		this.isVaccin = isVaccin;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/*
	 * public Integer getStorageUnitId() { return this.storageUnitId; }
	 * 
	 * public void setStorageUnitId(Integer storageUnitId) { this.storageUnitId =
	 * storageUnitId; }
	 */

	public Double getStrength() {
		return this.strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public FormulationType getFormulationType() {
		return this.formulationType;
	}

	public void setFormulationType(FormulationType formulationType) {
		this.formulationType = formulationType;
	}

	public Generic getGeneric() {
		return this.generic;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	public Manufacturer getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public StrengthUnit getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(StrengthUnit strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public PharmacologicalClassification getPharmacologicalClassification() {
		return this.pharmacologicalClassification;
	}

	public void setPharmacologicalClassification(PharmacologicalClassification pharmacologicalClassification) {
		this.pharmacologicalClassification = pharmacologicalClassification;
	}

	public List<Batch> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<Batch> batchList) {
		this.batchList = batchList;
	}

	public Tax getPurchaseTax() {
		return purchaseTax;
	}

	public List<UnitOfMeasurement> getUnitOfMeasurementList() {
		return unitOfMeasurementList;
	}

	public void setUnitOfMeasurementList(List<UnitOfMeasurement> unitOfMeasurementList) {
		this.unitOfMeasurementList = unitOfMeasurementList;
	}

	public void setPurchaseTax(Tax purchaseTax) {
		this.purchaseTax = purchaseTax;
	}

	public Tax getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(Tax salesTax) {
		this.salesTax = salesTax;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public List<StoreItemMapper> getStoreItemList() {
		return storeItemList;
	}

	public void setStoreItemList(List<StoreItemMapper> storeItemList) {
		this.storeItemList = storeItemList;
	}

	public Boolean getIsHospitalFormulary() {
		return isHospitalFormulary;
	}

	public void setIsHospitalFormulary(Boolean isHospitalFormulary) {
		this.isHospitalFormulary = isHospitalFormulary;
	}

	public List<StoreIndentDetail> getStoreIndentDetailList() {
		return storeIndentDetailList;
	}

	public void setStoreIndentDetailList(List<StoreIndentDetail> storeIndentDetailList) {
		this.storeIndentDetailList = storeIndentDetailList;
	}

	public List<ItemRequestDetail> getItemRequestDetailList() {
		return itemRequestDetailList;
	}

	public void setItemRequestDetailList(List<ItemRequestDetail> itemRequestDetailList) {
		this.itemRequestDetailList = itemRequestDetailList;
	}

	public void updateItemRestriction(Integer itemId) {
		this.id = itemId;

	}

	public StorageUnit getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(StorageUnit storageUnit) {
		this.storageUnit = storageUnit;
	}

	public Boolean getIsCssd() {
		return isCssd;
	}

	public void setIsCssd(Boolean isCssd) {
		this.isCssd = isCssd;
	}

	public Boolean getIsModality() {
		return isModality;
	}

	public void setIsModality(Boolean isModality) {
		this.isModality = isModality;
	}

	public Boolean getIsChargeableEqupiment() {
		return isChargeableEqupiment;
	}

	public void setIsChargeableEqupiment(Boolean isChargeableEqupiment) {
		this.isChargeableEqupiment = isChargeableEqupiment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public List<MedicationHistory> getMedicationHistoryList() {
		return medicationHistoryList;
	}

	public void setMedicationHistoryList(List<MedicationHistory> medicationHistoryList) {
		this.medicationHistoryList = medicationHistoryList;
	}

	public Boolean getIsSterilize() {
		return isSterilize;
	}

	public void setIsSterilize(Boolean isSterilize) {
		this.isSterilize = isSterilize;
	}

	public List<VaccinationMaster> getVaccinationMasterList() {
		return vaccinationMasterList;
	}

	public void setVaccinationMasterList(List<VaccinationMaster> vaccinationMasterList) {
		this.vaccinationMasterList = vaccinationMasterList;
	}

	public List<ImplantSpeciality> getImplantSpecialityList() {
		return implantSpecialityList;
	}

	public void setImplantSpecialityList(List<ImplantSpeciality> implantSpecialityList) {
		this.implantSpecialityList = implantSpecialityList;
	}
	public void update(String code, String productCode, String name, Boolean isConsignment, Boolean isSterilize)
	{
		this.code=code;
		this.productCode=productCode;
		this.name=name;
		this.isConsignment=isConsignment;
		this.isSterilize=isSterilize;
		
	}
	public ImplantSpeciality addItemDetailList(ImplantSpeciality itemDetail) {
		if (getImplantSpecialityList() == null)
			this.implantSpecialityList = new ArrayList<ImplantSpeciality>();
		
		getImplantSpecialityList().add(itemDetail);
		itemDetail.setItem(this);

		return itemDetail;
	}

	public ImplantSpeciality addImplantItemMAsterDetail(ImplantSpeciality implantSpeciality)
	{
		if (getImplantSpecialityList() == null)
			this.implantSpecialityList = new ArrayList<ImplantSpeciality>();
		
		getImplantSpecialityList().add(implantSpeciality);
		implantSpeciality.setItem(this);

		return implantSpeciality;
		
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
