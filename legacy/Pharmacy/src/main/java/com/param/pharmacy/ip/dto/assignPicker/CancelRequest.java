package com.param.pharmacy.ip.dto.assignPicker;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelRequest {
	
	Integer id;
	
	Integer statusId;
	private String cancelNote;
	private Boolean isFromAssignpicker;

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Boolean getIsFromAssignpicker() {
		return isFromAssignpicker;
	}

	public void setIsFromAssignpicker(Boolean isFromAssignpicker) {
		this.isFromAssignpicker = isFromAssignpicker;
	}
}
