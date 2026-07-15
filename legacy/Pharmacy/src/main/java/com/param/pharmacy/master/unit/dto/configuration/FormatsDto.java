package com.param.pharmacy.master.unit.dto.configuration;

public class FormatsDto {

	protected Integer id;

	protected String FormatName;
	
	protected String code;
	
	public FormatsDto() {
		super();
	}

	public FormatsDto(Integer id, String formatName) {
		super();
		this.id = id;
		FormatName = formatName;
	}
	
	public FormatsDto(Integer id, String formatName, String code) {
		super();
		this.id = id;
		FormatName = formatName;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFormatName() {
		return FormatName;
	}

	public void setFormatName(String formatName) {
		FormatName = formatName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
