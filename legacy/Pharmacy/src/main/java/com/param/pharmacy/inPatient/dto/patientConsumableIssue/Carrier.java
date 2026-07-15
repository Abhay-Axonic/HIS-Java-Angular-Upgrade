package com.param.pharmacy.inPatient.dto.patientConsumableIssue;

public class Carrier
{
	private Integer id;
	
	private String carrierName;
	
	private Boolean isSelected;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Carrier()
	{
		super();
	}
	
	public Carrier(Integer id, String carrierName) {
		super();
		this.id = id;
		this.carrierName = carrierName;
	}
	
	public Carrier(Integer id, String carrierName, Boolean isSelected) {
		
		this(id, carrierName);
		this.isSelected = isSelected;
	}
}