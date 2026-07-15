package com.param.global.nabhprocess.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.NabhReportingMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
@Transactional
public interface INabhReportingMapperService {
	Response saveNabhReportingMapper(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
	
	Response getNabhReportingMapperList(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;


	//Response updateNabhReportingMapper(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
	
	Response getNabhReportingMapperById(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
//
//	Response getnabhReportingMapperList(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
//
//	Response updatenabhReportingMapperStatus(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
//
//	Response getActivenabhReportingMapperList(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
//
//	Response getnabhReportingMapperCount(NabhReportingMapperDto nabhReportingMapperDto) throws ApplicationException;
//
//	Response getRoleWiseUserList(NabhReportingMapperDto nabhReportingMapperDto)throws ApplicationException;



}
