package com.param.opd.coversheet.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.dto.items.BatchResponse;

public class AdministrationWorklistDto {
private Integer encounterId;
	
	private Integer unitId;
	
	private Integer dose;

	private Integer organizationId;
	
	private Integer itemId;
	private Integer consumableQuantity;
	private Integer saleDetailId;

	private Integer patientId;
	private Integer batchId;

	private String batchNo;
	
	private String rackNo;

	private String uomType;

	private Integer uomTypeId;

	private Integer uomUnitId;
	private Integer storeId;
	private Integer prescriptionId;
	private Integer prescriptionDetailId;
	private Integer manufactureId;
	private String manufacturerName;
	private String itemCode;
	private String itemName;
	private Integer genericId;
	private Integer productCategoryId;
	private String genericName;
	private String roaDesc;
	private Double doseDesc;
	private List<BatchResponse> batch;
	private String strengthDesc;
	private Double taxPercentage;

	private Integer orderQty;
	private Integer quantity;
	private Integer statusId;
	private Integer markUpDown;
	private Double markUpDownPer;
	private Integer taxId;
	
	private String administrationDate;
	private String administrationTime;
	private String administrationRemark;
	private String unitName;
	private String expiryDate;
	private Integer administrationWorklistTimeDetailsId;
	private Boolean isHighRiskProduct;
	private Integer prescribeDoseUnitId;
	private String prescribeDoseUnit;
	private Double prescribeDose;
	private String dosageForm;
	private Integer factorId;
	private Integer formulationDosageMappId;
	private Integer duration;
	private String durationUnit;
	private Integer administratorQty;
	private Boolean antibioticTest;
	private Character isMedicationStop;
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "issueDetail", cascade = CascadeType.ALL)
	private List<IssueDetailBatchMapper> issueDetailBatchMapper;

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public List<IssueDetailBatchMapper> getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(List<IssueDetailBatchMapper> issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Integer getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getRoaDesc() {
		return roaDesc;
	}

	public void setRoaDesc(String roaDesc) {
		this.roaDesc = roaDesc;
	}

	public Double getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(Double doseDesc) {
		this.doseDesc = doseDesc;
	}

	public String getStrengthDesc() {
		return strengthDesc;
	}

	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getConsumableQuantity() {
		return consumableQuantity;
	}

	public void setConsumableQuantity(Integer consumableQuantity) {
		this.consumableQuantity = consumableQuantity;
	}

	public Integer getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public String getAdministrationDate() {
		return administrationDate;
	}

	public void setAdministrationDate(String administrationDate) {
		this.administrationDate = administrationDate;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getAdministrationRemark() {
		return administrationRemark;
	}

	public void setAdministrationRemark(String administrationRemark) {
		this.administrationRemark = administrationRemark;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getAdministrationWorklistTimeDetailsId() {
		return administrationWorklistTimeDetailsId;
	}

	public void setAdministrationWorklistTimeDetailsId(Integer administrationWorklistTimeDetailsId) {
		this.administrationWorklistTimeDetailsId = administrationWorklistTimeDetailsId;
	}

	public Boolean getIsHighRiskProduct() {
		return isHighRiskProduct;
	}

	public void setIsHighRiskProduct(Boolean isHighRiskProduct) {
		this.isHighRiskProduct = isHighRiskProduct;
	}

	public Integer getAdministratorQty() {
		return administratorQty;
	}

	public void setAdministratorQty(Integer administratorQty) {
		this.administratorQty = administratorQty;
	}

	public Integer getPrescribeDoseUnitId() {
		return prescribeDoseUnitId;
	}

	public void setPrescribeDoseUnitId(Integer prescribeDoseUnitId) {
		this.prescribeDoseUnitId = prescribeDoseUnitId;
	}

	public String getPrescribeDoseUnit() {
		return prescribeDoseUnit;
	}

	public void setPrescribeDoseUnit(String prescribeDoseUnit) {
		this.prescribeDoseUnit = prescribeDoseUnit;
	}

	public Double getPrescribeDose() {
		return prescribeDose;
	}

	public void setPrescribeDose(Double prescribeDose) {
		this.prescribeDose = prescribeDose;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public Integer getFactorId() {
		return factorId;
	}

	public void setFactorId(Integer factorId) {
		this.factorId = factorId;
	}

	public Integer getFormulationDosageMappId() {
		return formulationDosageMappId;
	}

	public void setFormulationDosageMappId(Integer formulationDosageMappId) {
		this.formulationDosageMappId = formulationDosageMappId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public Boolean getAntibioticTest() {
		return antibioticTest;
	}

	public void setAntibioticTest(Boolean antibioticTest) {
		this.antibioticTest = antibioticTest;
	}

	public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}
	
	
}
