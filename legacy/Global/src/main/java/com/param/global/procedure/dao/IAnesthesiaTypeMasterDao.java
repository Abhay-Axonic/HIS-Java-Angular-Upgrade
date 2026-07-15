package com.param.global.procedure.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.procedure.dto.AnesthesiaTypeMasterDto;
import com.param.otims.model.AnesthesiaTypeMaster;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IAnesthesiaTypeMasterDao extends IGenericDao<AnesthesiaTypeMaster, Integer>{
	
	public Response getAnesthesiaTypeList(int orgId, int unitId)throws ApplicationException;
	
	public Response saveAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response updateAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response updateAnesthesiaTypeMasterStatus(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response getListAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response getAnesthesiaTypeMasterById(Integer anesthesiaTypeId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getAnesthesiaTypeMasterCount(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response getAnesthesiaTypeMasterByName(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
	public Response getAnesthesiaTypeMasterByNameNotId(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto) throws ApplicationException;
	
}
