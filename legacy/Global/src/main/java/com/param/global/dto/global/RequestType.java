package com.param.global.dto.global;

public class RequestType {
	
	
	private Integer id;

	private String code;

	private String description;

	public RequestType() {
		super();
	}
	
	public RequestType(Integer id) {
		super();
		this.id = id;
	}

	public RequestType(Integer id, String code) {
		super();
		this.id = id;
		this.code = code;
	}
	public RequestType(Integer id, String code,String description) {
		super();
		this.id = id;
		this.code = code;
		this.description=description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
