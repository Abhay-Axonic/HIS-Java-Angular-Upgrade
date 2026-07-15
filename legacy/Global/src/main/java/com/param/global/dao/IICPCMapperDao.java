package com.param.global.dao;

import javax.servlet.http.HttpServletRequest;

import com.param.global.common.Response;
import com.param.global.dto.ICPCMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface IICPCMapperDao {

	Response saveICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response updateICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response updateICPCMapperStatus(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getICPCMapperById(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getICPCMapperList(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getICPCMapperCount(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getActiveICPCMapperList(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getGenderListByICPCSystemId(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response checkICPCMapperAvailable(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getSystemListForComplaintsOnCoversheet(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

//	Response getActiveGenderListForICPCMapper(ICPCMapperDto iCPCMapperDto)throws ApplicationException;

	Response getActiveGenderListForICPCMapper(HttpServletRequest request);

	Response getICPCByName(String icpcName)throws ApplicationException;

	Response getICPCByCode(String code) throws ApplicationException;
}
