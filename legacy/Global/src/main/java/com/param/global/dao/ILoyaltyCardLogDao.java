package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.LoyaltyCardLogDto;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ILoyaltyCardLogDao {
	
	public Response saveLoyaltyCardLog(LoyaltyCardLogDto loyaltyCardLogDto) throws ApplicationException;

	public Response getLogDataByCardNo(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

	public Response updateLogs(LoyaltyCardLogDto loyaltyCardLogDto)throws ApplicationException;

}
