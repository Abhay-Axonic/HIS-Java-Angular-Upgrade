package com.param.global.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NursingStationMasterDto;
@Transactional
@SuppressWarnings("rawtypes")
public interface INursingStationService {


	public Response getListOfNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException;
	
	public Response getListOfNursingStationMasterByClinicId(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException;
	
	public Response saveNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException;
	
	public Response getNursingStationMasterById(int nursingId, int orgId, int unitId) throws ApplicationException;
	
	public Response getNursingStationMasterList(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
	
	public Response updateNursingStationMaster(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
	
	public Response updateNursingStationMasterStatus(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
	
	public Response getNursingStationMasterCount(NursingStationMasterDto nursingStationMasterDto) throws ApplicationException;
}
