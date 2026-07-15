package com.param.global.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.DataObjectMasterDto;
import com.param.global.model.DataObjectMaster;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IDataObjectMasterDao extends IGenericDao<DataObjectMaster, Integer>{
	public Response saveDataObject(DataObjectMasterDto dto)throws ApplicationException;
	public Response checkAlreadyExists(int orgId, int unitId, String title)throws ApplicationException;
	public Response getDataObjectList(int orgId, int unitId)throws ApplicationException;
	public Response getDataObjectById(int id)throws ApplicationException;
	public Response getAllDataObject(int orgId, int unitId)throws ApplicationException;
	public Response updateDataObjectStatus(DataObjectMasterDto dataObjectMasterDto)throws ApplicationException;
	public Response getDataObjectByNameNotId(DataObjectMasterDto reqDto)throws ApplicationException;
}
