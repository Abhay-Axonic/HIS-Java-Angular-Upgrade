package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.StoreTypeMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IStoreTypeMasterDao {
	
	Response saveStoreType(StoreTypeMasterDto dto) throws ApplicationException;

	Response updateStoreType(StoreTypeMasterDto dto) throws ApplicationException;

	Response updateStoreTypeStatus(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeList(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeCount(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeById(StoreTypeMasterDto dto) throws ApplicationException;
	
	Response getStoreTypeAlreadyExistOrNot(StoreTypeMasterDto dto) throws ApplicationException;
	
	Response getStoreTypeAlreadyExistOrNotById(StoreTypeMasterDto dto) throws ApplicationException;

}
