package com.param.global.service;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.DataObjectMasterDto;

@SuppressWarnings({"rawtypes"})
public interface IDataObjectMasterService {
	public Response saveDataObject(DataObjectMasterDto dto)throws ApplicationException;
	public Response getDataObjectList(int orgId, int unitId)throws ApplicationException;
	public Response getDataObjectById(int id)throws ApplicationException;
	public Response getAllDataObject(int orgId, int unitId)throws ApplicationException;
	public Response updateDataObjectStatus(DataObjectMasterDto dataObjectMasterDto)throws ApplicationException;
	public Response updateDataObject(DataObjectMasterDto reqDto)throws ApplicationException;
}
