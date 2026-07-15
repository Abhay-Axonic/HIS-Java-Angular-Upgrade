package com.param.pharmacy.master.unit.dto.vendor;

public class StateDto {

	protected Integer stateId;
	
	protected String stateName;
	
	public StateDto(Integer stateId, String stateName) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
}
