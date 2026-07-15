package com.param.pharmacy.master.unit.dto.storewiseGenericMapper;

public class GenericResponseDto {

	private Integer id;
	private String genericCode;
	private String genericName;

	public GenericResponseDto(Integer id, String genericCode, String genericName) {
		super();
		this.id = id;
		this.genericCode = genericCode;
		this.genericName = genericName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGenericCode() {
		return genericCode;
	}

	public void setGenericCode(String genericCode) {
		this.genericCode = genericCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

}
