package com.param.pharmacy.master.unit.dto.vendor;

import com.param.service.global.model.CookieObjectDto;

public abstract class VendorResponse extends CookieObjectDto {

	protected Integer id;

	protected String code;

	protected String name;

	protected String address;
	
	protected String country;
	
	protected String state;
	
	protected String city;
	
	protected String area;
	
	protected Integer countryId;
	
	protected Integer stateId;
	
	protected Integer cityId;
	
	protected Integer areaId;
	
	protected String zipcode;

	protected String contactPersonName;

	protected String drugLicenseNo;

	protected String emailId;

	protected String gstNo;

	protected Boolean isLocalPurchase;

	protected String paymentTerm;

	protected String phoneNumber;

	protected String manufacturerName;

	protected String paymentModeDesc;

	protected Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getDrugLicenseNo() {
		return drugLicenseNo;
	}

	public void setDrugLicenseNo(String drugLicenseNo) {
		this.drugLicenseNo = drugLicenseNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public Boolean getIsLocalPurchase() {
		return isLocalPurchase;
	}

	public void setIsLocalPurchase(Boolean isLocalPurchase) {
		this.isLocalPurchase = isLocalPurchase;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getPaymentModeDesc() {
		return paymentModeDesc;
	}

	public void setPaymentModeDesc(String paymentModeDesc) {
		this.paymentModeDesc = paymentModeDesc;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id": 1 "code": "name": "manufacturerId" "manufacturerName" "paymentModeId"
 * "paymentModeDesc" "contactPersonName" "drugLicenseNo" "gstNo" "phoneNumber"
 * "emailId" "paymentTerm" "isLocalPurchase" "status": true
 */