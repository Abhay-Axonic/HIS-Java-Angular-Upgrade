package com.param.pharmacy.master.unit.genralinsurancedto;

public class GeneralInsuranceRequest extends GenralInsuranceResponse {
	
	protected Integer id;

	protected Integer areaId;
	
	protected Integer cityId;
	
	protected Integer countryId;
	
	protected Integer stateId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	// Previously overridden here to unconditionally `return null;` (a leftover
	// stub), shadowing the real getter/setter pair inherited from
	// GenralInsuranceResponse -- silently discarded the zip code on every
	// Create (nulled outright) and Update (the null-check guard around
	// setZipcode() meant the field was silently skipped instead of applied).
	// Removed; the inherited getter is now used.

}
