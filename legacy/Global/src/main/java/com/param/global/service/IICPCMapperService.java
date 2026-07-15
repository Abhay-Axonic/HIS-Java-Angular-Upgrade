package com.param.global.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.ICPCMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface IICPCMapperService {
	@Transactional
	Response saveICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response updateICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response updateICPCMapperStatus(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getICPCMapperById(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getICPCMapperList(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getICPCMapperCount(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getActiveICPCMapperList(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getGenderListByICPCSystemId(ICPCMapperDto iCPCMapperDto)throws ApplicationException;
	
	@Transactional
	Response checkICPCMapperAvailable(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getSystemListForComplaintsOnCoversheet(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

//	@Transactional
//	Response getActiveGenderListForICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	@Transactional
	Response getActiveGenderListForICPCMapper(HttpServletRequest request);

	@Transactional
	Response getICPCByName(String icpcName);

	@Transactional
    Response getICPCByCode(String icpcName);
}
