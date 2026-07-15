package com.param.pharmacy.master.unit.dto.vendor;

import java.util.List;

public class VendorRequest extends VendorResponse{

	protected Integer manufacturerId;

	protected Integer paymentModeId;
	
	protected Integer areaId;
	
	protected Integer cityId;
	
	protected Integer countryId;
	
	protected Integer stateId;
	
    protected String creditDays;
    
    protected Integer generalLedgerId;

	protected List<ManufacturerIds> manufacturerIds;
	public List<ManufacturerIds> getManufacturerIds() {
		return manufacturerIds;
	}

	public void setManufacturerIds(List<ManufacturerIds> manufacturerIds) {
		this.manufacturerIds = manufacturerIds;
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

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	
    public String getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(String creditDays) {
        this.creditDays = creditDays;
    }

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

}
