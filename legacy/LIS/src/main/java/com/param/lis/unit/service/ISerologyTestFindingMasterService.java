package com.param.lis.unit.service;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.SerologyTestFindingMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISerologyTestFindingMasterService {

	public Response saveSerologyTestMaster(SerologyTestFindingMasterDto headerMasterDto) throws ApplicationException;

	public Response getSerologyTestMasterById(Integer orgId, Integer unitId, Integer serologyId)
			throws ApplicationException;

	public Response updateSerologyMaster(SerologyTestFindingMasterDto serologyTestMasterDto) throws ApplicationException;

	public Response ActivateInactivateSerologyTestMaster(Integer orgId, Integer serologyId, Character status)
			throws ApplicationException;

	public Response listSerologyMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getToTalSerologyMasterRecord(Integer orgId, Integer unitId) throws ApplicationException;

}
