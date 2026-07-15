package com.param.global.unit.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.unit.dto.VisitorPassMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IVisitorPassMapperService {

	
	Response saveVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException;

	Response updateVisitorPassMapper(VisitorPassMapperDto dto)throws ApplicationException;

	Response updateVisitorPassMapperStatus(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperList(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperCount(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperById(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(VisitorPassMapperDto dto)throws ApplicationException;

	Response getVisitorPassMapperListByBedCategoryNVisitType(VisitorPassMapperDto dto)throws ApplicationException;

}
