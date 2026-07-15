package com.param.global.unit.dao;

import com.param.global.unit.dto.VisitorPassMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IVisitorPassMapperDao {

	
	Response saveVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException;

	Response updateVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException;

	Response updateVisitorPassMapperStatus(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperList(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperCount(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperById(VisitorPassMapperDto dto)throws ApplicationException;

	Response checkVisitorPassMapperAlreadyExistOrNot(VisitorPassMapperDto dto)throws ApplicationException;

	Response checkVisitorPassMapperAlreadyExistOrNotById(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(VisitorPassMapperDto dto) throws ApplicationException;

	Response getVisitorPassMapperListByBedCategoryNVisitType(VisitorPassMapperDto dto)throws ApplicationException;

}
