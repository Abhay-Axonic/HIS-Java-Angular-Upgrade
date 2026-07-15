package com.param.lis.unit.dao;

import com.param.data.dto.HeaderParameterMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IHeaderParameterMapperDao {

	Response saveHeaderParameterMapper(HeaderParameterMapperDto dto)throws ApplicationException;

}
