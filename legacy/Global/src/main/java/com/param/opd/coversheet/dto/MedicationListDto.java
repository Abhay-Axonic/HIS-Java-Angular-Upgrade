package com.param.opd.coversheet.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import scala.math.BigInt;

public class MedicationListDto {
	
	private Integer drugId;
	
	private String medicationName;
	
	private Integer genericId;
	
	private String genericName;
	
	private Integer formulationTypeId;
	
	private Integer uomId;
	
	private String uom;
	
	private String unit;
	
	private Integer conversions;
	private Boolean isNarcotic;
	private boolean isDoseCalculationApplicable;
	private String doseCalculationApplicableData;

	private Integer unitId;
	private Integer organizationId;
	private Integer uomTypeId;
	private Integer strengthUnitId;
	private String unitDesc;

	private Double strength;
	private BigDecimal rate;

	private Double availableQty;
	
	private List<RoaMasterDto> roaMasterDtoList;
	
	private BigInteger genericCount;
	
	private BigInteger Qty;
	
	private String cimsCode;
	private String searchItem;
	
	private String cimsType;
	private List<MedicationListDto> listSubstitute;
	public BigInteger getQty() {
		return Qty;
	}

	public void setQty(BigInteger qty) {
		Qty = qty;
	}

	public BigInteger getGenericCount() {
		return genericCount;
	}

	public void setGenericCount(BigInteger genericCount) {
		this.genericCount = genericCount;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getConversions() {
		return conversions;
	}

	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public List<RoaMasterDto> getRoaMasterDtoList() {
		return roaMasterDtoList;
	}

	public void setRoaMasterDtoList(List<RoaMasterDto> roaMasterDtoList) {
		this.roaMasterDtoList = roaMasterDtoList;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
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

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public Double getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Double availableQty) {
		this.availableQty = availableQty;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public boolean getIsDoseCalculationApplicable() {
		return isDoseCalculationApplicable;
	}

	public void setDoseCalculationApplicable(boolean isDoseCalculationApplicable) {
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

	public List<MedicationListDto> getListSubstitute() {
		return listSubstitute;
	}

	public void setListSubstitute(List<MedicationListDto> listSubstitute) {
		this.listSubstitute = listSubstitute;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getSearchItem() {
		return searchItem;
	}

	public void setSearchItem(String searchItem) {
		this.searchItem = searchItem;
	}
}
