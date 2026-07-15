package com.param.CSSD.dto;

import java.util.List;

public class KitFormationRequestDto {
	
	private Integer transitionNumber;
	private Integer wrapperId;
	private Integer wrapperQuantity;
	private List<PreSterilizationProcessInstrumentDto> equipmentList;
	public Integer getTransitionNumber() {
		return transitionNumber;
	}
	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}
	public Integer getWrapperId() {
		return wrapperId;
	}
	public void setWrapperId(Integer wrapperId) {
		this.wrapperId = wrapperId;
	}
	public Integer getWrapperQuantity() {
		return wrapperQuantity;
	}
	public void setWrapperQuantity(Integer wrapperQuantity) {
		this.wrapperQuantity = wrapperQuantity;
	}
	public List<PreSterilizationProcessInstrumentDto> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<PreSterilizationProcessInstrumentDto> equipmentList) {
		this.equipmentList = equipmentList;
	}
	
	
	

}
