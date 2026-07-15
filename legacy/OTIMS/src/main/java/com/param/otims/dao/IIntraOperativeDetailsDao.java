package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.IntraOperativeImageDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IIntraOperativeDetailsDao {

	Response savePreOperativeDetails(IntraOperativeDetailsDto intraOperativeDetailsDto)throws ApplicationException;

	Response getIntraOperativeByProReqId(IntraOperativeDetailsDto dto)throws ApplicationException;

	Response saveIntraVideo(IntraOperativeImageDto dto);

}
