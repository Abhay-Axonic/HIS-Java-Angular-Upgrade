package com.param.pharmacy.master.global;

public class CustomeException extends Exception {
	private Long id;
	private String message;
	private Boolean status;

	public CustomeException(Long id, String message, Boolean status) {
		super();
		this.id = id;
		this.message = message;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
