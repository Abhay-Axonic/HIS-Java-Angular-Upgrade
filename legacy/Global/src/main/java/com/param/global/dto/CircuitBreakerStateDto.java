package com.param.global.dto;

import java.util.Date;

public class CircuitBreakerStateDto {

	private Integer id;
	private String state;
	private String tripReason;
	private Date updatedDate;
	private Integer updatedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTripReason() {
		return tripReason;
	}

	public void setTripReason(String tripReason) {
		this.tripReason = tripReason;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
}
