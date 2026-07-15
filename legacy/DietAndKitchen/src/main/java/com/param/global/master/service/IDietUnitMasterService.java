package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.UnitOfMeasurmentMasterDto;

import in.param.exception.ApplicationException;
@Transactional
public interface IDietUnitMasterService {

	public Response saveDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException ;

	public Response updateDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)  throws ApplicationException;

	public Response getDietUnitMasterCount(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)  throws ApplicationException;

	public Response getActiveDietUnitMaster(Integer organizationId)  throws ApplicationException;

	public Response getDietUnitMasterList(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)  throws ApplicationException;

	public Response getDietUnitMasterById(Integer cabinId, Integer organizationId)  throws ApplicationException;

	public Response updateDietUnitMasterStatus(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)  throws ApplicationException;

}
