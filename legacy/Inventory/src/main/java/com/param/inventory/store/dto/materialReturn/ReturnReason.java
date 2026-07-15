package com.param.inventory.store.dto.materialReturn;

public class ReturnReason {

	protected Integer id;

	protected String reason;

	public ReturnReason(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}

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

}
