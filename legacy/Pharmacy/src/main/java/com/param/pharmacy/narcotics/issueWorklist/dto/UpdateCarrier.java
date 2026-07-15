package com.param.pharmacy.narcotics.issueWorklist.dto;

import com.param.service.global.model.CookieObjectDto;

public class UpdateCarrier extends CookieObjectDto
{
	private Long id;
	
	private Integer carrierId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}
	
}