package com.param.global.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.StoreTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IStoreTypeMasterService {

	Response saveStoreType(StoreTypeMasterDto dto) throws ApplicationException;

	Response updateStoreType(StoreTypeMasterDto dto) throws ApplicationException;

	Response updateStoreTypeStatus(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeList(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeCount(StoreTypeMasterDto dto) throws ApplicationException;

	Response getStoreTypeById(StoreTypeMasterDto dto) throws ApplicationException;

}
