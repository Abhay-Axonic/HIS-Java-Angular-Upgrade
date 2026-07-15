package com.param.procurement.quotation.dto.quotationFill;

public class VendorDetails {

	private String contactPerson;

	private String address;

	private String country;

	private String state;

	private String city;

	private String area;

	private String zipCode;
	
	
	public VendorDetails(String contactPerson, String address, String country, String state, String city, String area,
			String zipCode) {
		super();
		this.contactPerson = contactPerson;
		this.address = address;
		this.country = country;
		this.state = state;
		this.city = city;
		this.area = area;
		this.zipCode = zipCode;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
	

}
