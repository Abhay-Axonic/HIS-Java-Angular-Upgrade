package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.IntraOperativeImageDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IIntraOperativeDetailsService {

	Response saveIntraOperativeDetails(IntraOperativeDetailsDto intraOperativeDetailsDto)throws ApplicationException;

	Response saveIntraImage(IntraOperativeImageDto dto)throws ApplicationException;

	Response getIntraOperativeByProReqId(IntraOperativeDetailsDto dto)throws ApplicationException;

	Response saveIntraVideo(IntraOperativeImageDto dto)throws ApplicationException;

}
