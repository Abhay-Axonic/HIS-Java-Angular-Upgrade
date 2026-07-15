package com.param.CSSD.service;

import java.util.List;

import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IPreSterilizationProcessInstrumentService {

	/**
	 * This method is used to update equipments in the kits for pre sterilization process.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	Response updatePreSterilizationProcessEquipment(PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto);

	/**
	 * This method is used to get the list of equipments in the particular kit.
	 * @param kitId
	 * @return Response
	 */
	Response getPreSterilizationProcessEquipmentList(Integer kitId);

	/**
	 * This method is used to clean the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	Response cleanEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList);
	
	/**
	 * This method is used to get the list of equipments pending for inspection.
	 * @param kitId
	 * @return Response
	 */
	Response getEquipmentListPendingForInspection(Integer kitId);
	
	/**
	 * This method is used to get the list of equipments pending for dis infection.
	 * @param kitId
	 * @return Response
	 */
	Response getEquipmentListPendingForDisInfection(Integer kitId);
	
	/**
	 * This method is used to get the list of equipments pending for kit formation.
	 * @param kitId
	 * @return Response
	 */
	Response getEquipmentListPendingForKitFormation(Integer kitId);
	
	/**
	 * This method is used to inspect the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	Response inspectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList);
	
	/**
	 * This method is used to dis infect the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	Response disInfectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList);
}
