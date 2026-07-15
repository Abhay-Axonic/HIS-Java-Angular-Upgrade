package com.param.pharmacy.master.unit.dto.mainStore;

public class VisitTypeRabbitDto {

	private Integer visit_type_id;
	
	private String visit_type_name;
	
	private String visit_type_code;
	
	public VisitTypeRabbitDto() {
		super();
	}
	
	public VisitTypeRabbitDto(Integer visit_type_id, String visit_type_name, String visit_type_code) {
		super();
		this.visit_type_id = visit_type_id;
		this.visit_type_name = visit_type_name;
		this.visit_type_code = visit_type_code;
	}

	public Integer getVisit_type_id() {
		return visit_type_id;
	}

	public void setVisit_type_id(Integer visit_type_id) {
		this.visit_type_id = visit_type_id;
	}

	public String getVisit_type_name() {
		return visit_type_name;
	}

	public void setVisit_type_name(String visit_type_name) {
		this.visit_type_name = visit_type_name;
	}

	public String getVisit_type_code() {
		return visit_type_code;
	}

	public void setVisit_type_code(String visit_type_code) {
		this.visit_type_code = visit_type_code;
	}
	
}
