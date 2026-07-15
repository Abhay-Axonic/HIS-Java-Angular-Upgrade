package com.param.global.opd.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.model.EncounterDetails;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IEncounterAssignmenMapperDao extends IGenericDao<EncounterDetails, Integer> {

	public Response saveEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException;
	
	public Response getOldListEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException;

	Response updateEncounterAssignment(EncounterAssignmenMapperDto encounterAssignmenMapperDto)throws ApplicationException;

	public Response getOldConsulataions(Integer admissionId)throws ApplicationException;

	Response getCabinListByDoctorId(Integer doctorId, Integer unitId)throws ApplicationException;

	public Response getCabinListByRMOMappedDoctorList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

}
