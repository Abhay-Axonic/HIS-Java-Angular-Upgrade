package com.param.procurement.quotation.dto.quotationRequest;

public class QRAccessoriesMapper {
	
    private Integer id;
	
	private String details;
	
	private String name;

	public QRAccessoriesMapper() {
		super();
	}
	
	public QRAccessoriesMapper(Integer id, String details, String name) {
		super();
		this.id = id;
		this.details = details;
		this.name = name;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	

}
