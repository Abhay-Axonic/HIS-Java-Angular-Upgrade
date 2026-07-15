 package com.param.pharmacy.ip.dto.pharmacology;

import com.param.service.global.model.CookieObjectDto;

public class UpdateReceiver extends CookieObjectDto
{
	private Long id;	
	
	private String receiverName;
	
	private Integer statusId;
	
	private Integer carrierId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}


	
}