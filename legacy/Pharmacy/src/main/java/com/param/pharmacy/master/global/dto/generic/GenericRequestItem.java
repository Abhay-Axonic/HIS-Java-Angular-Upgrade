package com.param.pharmacy.master.global.dto.generic;

public class GenericRequestItem {
	
	private Integer id;
	
	private Integer genericId;

	private String genericName;
	
	private Integer subGenericId;
	
	private Double strengthUnit;

	private Integer strength;

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getSubGenericId() {
		return subGenericId;
	}

	public void setSubGenericId(Integer subGenericId) {
		this.subGenericId = subGenericId;
	}

	public Double getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(Double strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public GenericRequestItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GenericRequestItem(Integer id, Integer genericId,String genericName,
							  Integer subGenericId, Double strengthUnit, Integer strength) {
		super();
		this.id = id;
		this.genericId = genericId;
		this.subGenericId = subGenericId;
		this.strengthUnit = strengthUnit;
		this.strength = strength;
		this.genericName = genericName;
	}
	
	
	

}
