package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ITransfusionTransmittedDiseasesService {

	public Response saveTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException;

	public Response searchTTD(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

	public Response getTTDDetailsById(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException;

	public Response getTTDListOnRedCellGroupingAndSerum(
			TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

}
