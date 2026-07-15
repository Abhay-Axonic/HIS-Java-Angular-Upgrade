package com.param.global.opd.service;

import com.param.global.common.Response;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IEncounterAssignmenMapperService {

	public Response saveEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException;
	
	public Response getOldListEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException;

	Response updateEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException;

	Response updatePatientCritical(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getCabinListByDoctorId(Integer doctorId, Integer unitId)
			throws ApplicationException;

	Response saveEncounterAssignmentIfAssessignmentIsFalse(EncounterMasterDto encounterMasterDto)
			throws ApplicationException;

	public Response getCabinListByRMOMappedDoctorList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateEncounterDetailsMultilingual(EncounterAssignmenMapperDto encounterAssignmenMapperDto)
			throws ApplicationException;
	
	
}
