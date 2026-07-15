package com.param.linenLaundry.linenIssue.dto;

public class Carrier
{
	private Integer id;
	
	private String carrierName;

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

	public Carrier()
	{
		super();
	}
	
	public Carrier(Integer id, String carrierName) {
		super();
		this.id = id;
		this.carrierName = carrierName;
	}
	
}