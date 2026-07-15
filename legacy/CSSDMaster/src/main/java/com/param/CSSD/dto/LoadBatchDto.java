package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.entity.model.master.Store;

@JsonInclude(Include.NON_NULL)
public class LoadBatchDto {

	private Integer batchNumber;

	private Integer autoclaveNumber;
	
	private String autoclaveName;
	
	private AutoclaveMaster  autoclaveMaster;
	
	private List<PreSterilizationProcessDto>  kitList;
	
	private String autoclaveDate;
	
	private String expiryDate;
	
	private Boolean biologicalIndicator;

	private Integer created_by;

	private Integer updated_by;

	private String created_date;

	private String updated_date;
	
	private String control;
	
	private String test;
	
	private String status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer storeId;
	
	private Store store;
	
	private Integer sterilizationTemperature;
	
	private Integer pressure;
	
	private String color;
	
	private String sterilizationTypeDesc;
	
	private Boolean isSterilized;
	
	private Integer sterilizationColorId;

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getAutoclaveNumber() {
		return autoclaveNumber;
	}

	public void setAutoclaveNumber(Integer autoclaveNumber) {
		this.autoclaveNumber = autoclaveNumber;
	}

	public AutoclaveMaster getAutoclaveMaster() {
		return autoclaveMaster;
	}

	public void setAutoclaveMaster(AutoclaveMaster autoclaveMaster) {
		this.autoclaveMaster = autoclaveMaster;
	}
	
	public String getAutoclaveDate() {
		return autoclaveDate;
	}

	public void setAutoclaveDate(String autoclaveDate) {
		this.autoclaveDate = autoclaveDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getBiologicalIndicator() {
		return biologicalIndicator;
	}

	public void setBiologicalIndicator(Boolean biologicalIndicator) {
		this.biologicalIndicator = biologicalIndicator;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PreSterilizationProcessDto> getKitList() {
		return kitList;
	}

	public void setKitList(List<PreSterilizationProcessDto> kitList) {
		this.kitList = kitList;
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

	public Integer getSterilizationTemperature() {
		return sterilizationTemperature;
	}

	public void setSterilizationTemperature(Integer sterilizationTemperature) {
		this.sterilizationTemperature = sterilizationTemperature;
	}

	public Integer getPressure() {
		return pressure;
	}

	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAutoclaveName() {
		return autoclaveName;
	}

	public void setAutoclaveName(String autoclaveName) {
		this.autoclaveName = autoclaveName;
	}

	public String getSterilizationTypeDesc() {
		return sterilizationTypeDesc;
	}

	public void setSterilizationTypeDesc(String sterilizationTypeDesc) {
		this.sterilizationTypeDesc = sterilizationTypeDesc;
	}

	public Boolean getIsSterilized() {
		return isSterilized;
	}

	public void setIsSterilized(Boolean isSterilized) {
		this.isSterilized = isSterilized;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Integer getSterilizationColorId() {
		return sterilizationColorId;
	}

	public void setSterilizationColorId(Integer sterilizationColorId) {
		this.sterilizationColorId = sterilizationColorId;
	}

	
	
}
