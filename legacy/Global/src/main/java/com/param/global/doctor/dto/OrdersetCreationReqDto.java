package com.param.global.doctor.dto;

import java.util.List;

public class OrdersetCreationReqDto {
	private OrdersetMasterDto ordersetMaster;
	private List<OrdersetMedicationDetailsDto> ordersetMedications;
	private List<OrdersetInvestigationDetailsDto> ordersetInvestigations;
	public OrdersetMasterDto getOrdersetMaster() {
		return ordersetMaster;
	}
	public void setOrdersetMaster(OrdersetMasterDto ordersetMaster) {
		this.ordersetMaster = ordersetMaster;
	}
	public List<OrdersetMedicationDetailsDto> getOrdersetMedications() {
		return ordersetMedications;
	}
	public void setOrdersetMedications(List<OrdersetMedicationDetailsDto> ordersetMedications) {
		this.ordersetMedications = ordersetMedications;
	}
	public List<OrdersetInvestigationDetailsDto> getOrdersetInvestigations() {
		return ordersetInvestigations;
	}
	public void setOrdersetInvestigations(List<OrdersetInvestigationDetailsDto> ordersetInvestigations) {
		this.ordersetInvestigations = ordersetInvestigations;
	}
}
