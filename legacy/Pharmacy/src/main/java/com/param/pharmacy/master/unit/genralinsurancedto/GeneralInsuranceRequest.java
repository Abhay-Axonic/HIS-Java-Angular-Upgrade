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

	public String getZipCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
