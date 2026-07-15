package com.param.global.otims.dto;

import java.util.List;

public class OTTDetailsVO {
	
	private Integer theatreTypeId;
	
	private String theatreTypeName;
	
	private List<OperationTheatreDto> operationTheatreList;

	public Integer getTheatreTypeId() {
		return theatreTypeId;
	}

	public void setTheatreTypeId(Integer theatreTypeId) {
		this.theatreTypeId = theatreTypeId;
	}

	public String getTheatreTypeName() {
		return theatreTypeName;
	}

	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}

	public List<OperationTheatreDto> getOperationTheatreList() {
		return operationTheatreList;
	}

	public void setOperationTheatreList(List<OperationTheatreDto> operationTheatreList) {
		this.operationTheatreList = operationTheatreList;
	}
	
	

}
