package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.NursingOrderMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface INursingOrderMasterService {
	
	public Response saveNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response updateNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response updateNursingOrderMasterStatus(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response getNursingOrderMasterById(Integer nursingOrderId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getNursingOrderMasterList(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response getNursingOrderMasterCount(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response getActiveNursingOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getNursingOrderMasterByName(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException;

	public Response getNursingOrderMasterByNameNotId(NursingOrderMasterDto nursingOrderMasterDto)throws ApplicationException;

	public Response getNursingOrderMasterListSearch(NursingOrderMasterDto nursingOrderMasterDto)throws ApplicationException;


}
