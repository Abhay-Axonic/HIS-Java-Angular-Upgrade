package com.param.lis.unit.dao;


import com.param.entity.lis.unit.SerologyTestFindingMaster;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.SerologyTestFindingMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISerologyTestFindingMasterDao extends IGenericDao<SerologyTestFindingMaster, Integer> {

	public Response saveSerologyTestMaster(SerologyTestFindingMasterDto serologyTestMasterDto) throws ApplicationException;

	public Response getSerologyTestMasterById(Integer orgId, Integer unitId, Integer serologyId)
			throws ApplicationException;

	public Response updateSerologyMaster(SerologyTestFindingMasterDto seroTestMasterDto) throws ApplicationException;

	public Response ActivateInactivateSerologyMaster(Integer orgId, Integer serologyId, Character serologyStatus)
			throws ApplicationException;

	public Response listSerologyMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getToTalSerologyMasterRecord(Integer orgId, Integer unitId) throws ApplicationException;

	public Response checkForUniqueSerologyTestMasterDesc(SerologyTestFindingMasterDto serologyTestMasterDto) throws ApplicationException;

	public Response updateUniqueSerologyTestMasterDesc(SerologyTestFindingMasterDto serologyTestMasterDto)throws ApplicationException;

}
