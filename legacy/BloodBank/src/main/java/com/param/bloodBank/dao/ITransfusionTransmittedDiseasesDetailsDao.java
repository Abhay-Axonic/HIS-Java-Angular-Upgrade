package com.param.bloodBank.dao;

import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDetailsDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface ITransfusionTransmittedDiseasesDetailsDao {

	public Response saveTransfusionTransmittedDiseasesDetails(TransfusionTransmittedDiseasesDetailsDto dto) throws ApplicationException;

	public Response getTTDDetailsById(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

	public Response updateTransfusionTransmittedDiseasesDetails(TransfusionTransmittedDiseasesDetailsDto dto)throws ApplicationException;

}
