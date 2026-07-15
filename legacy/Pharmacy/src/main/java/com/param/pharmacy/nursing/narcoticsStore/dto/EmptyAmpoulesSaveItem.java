package com.param.pharmacy.nursing.narcoticsStore.dto;

public class EmptyAmpoulesSaveItem {
	private Integer id;

	private Integer quantityReturned;

	private Integer quantityAccepted;
	private Integer quantityRejected;

	public Integer getQuantityRejected() {
		return quantityRejected;
	}

	public void setQuantityRejected(Integer quantityRejected) {
		this.quantityRejected = quantityRejected;
	}

	private Integer statusId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(Integer quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public Integer getQuantityAccepted() {
		return quantityAccepted;
	}

	public void setQuantityAccepted(Integer quantityAccepted) {
		this.quantityAccepted = quantityAccepted;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

}