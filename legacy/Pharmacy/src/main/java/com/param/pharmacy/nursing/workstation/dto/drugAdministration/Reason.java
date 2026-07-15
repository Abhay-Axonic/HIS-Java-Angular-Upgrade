package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Reason
{
	private Integer id;
	
	private String reason;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Reason(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}
}