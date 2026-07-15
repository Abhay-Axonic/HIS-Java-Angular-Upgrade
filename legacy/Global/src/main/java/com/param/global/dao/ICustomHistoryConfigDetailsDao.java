package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICustomHistoryConfigDetailsDao {
	public Response getCustomHistoryConfigurationList(CustomHistoryConfigurationDetailsDto customHistoryConfigurationDetailsDto)throws ApplicationException;

	public Response saveCustomHistoryConfigurationDetails(CustomHistoryConfigurationDetailsDto dtlsDto)throws ApplicationException;

	public Response updateCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto)throws ApplicationException;

	public Response deleteCustomHistoryDetailsRecord(CustomHistoryConfigurationDetailsDto dto)throws ApplicationException;

	public Response getPatientCustomHistoryDetails(CustomHistoryConfigurationDetailsDto dto)throws ApplicationException;
	
}
