package com.param.pharmacy.master.unit.dto.vendor;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.generic.Manufacturer;
public class VendorDropdownRespose {
	protected List<Manufacturer> manufacturer;
	protected List<CountryDto> country;
	protected List<PaymentModeDto> paymentMode;
	private Integer mobileDigit;

	public List<Manufacturer> getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(List<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<CountryDto> getCountry() {
		return country;
	}

	public void setCountry(List<CountryDto> country) {
		this.country = country;
	}

	public List<PaymentModeDto> getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(List<PaymentModeDto> paymentMode) {
		this.paymentMode = paymentMode;
	}

	public void addManufacturer(Manufacturer manufacturer) {
		if (this.manufacturer == null)
			this.manufacturer = new ArrayList<Manufacturer>();

		this.manufacturer.add(manufacturer);
	}

	public void addCountry(CountryDto countryDto) {
		if (this.country == null)
			this.country = new ArrayList<CountryDto>();

		this.country.add(countryDto);
	}

	public void addPaymentMode(PaymentModeDto paymentModeDto) {
		if (this.paymentMode == null)
			this.paymentMode = new ArrayList<PaymentModeDto>();

		this.paymentMode.add(paymentModeDto);
	}

	public Integer getMobileDigit() {
		return mobileDigit;
	}

	public void setMobileDigit(Integer mobileDigit) {
		this.mobileDigit = mobileDigit;
	}
}
