package com.param.global.dto.global;

public class TaxDropDown {
	
	private Integer taxId;

	private String taxName;

	private Double taxPercent;

	public TaxDropDown() {
		super();
	}

	public TaxDropDown(Integer taxId, String taxName, Double taxPercent) {
		super();
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}
	
}
