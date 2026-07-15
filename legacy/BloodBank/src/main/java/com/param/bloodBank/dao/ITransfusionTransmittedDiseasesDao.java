package com.param.bloodBank.dao;

import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ITransfusionTransmittedDiseasesDao {

	public Response checkUniqueReq(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

	public Response saveTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

	public Response searchTTD(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException;

	public Response updateTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException;

	public Response getTTDListOnRedCellGroupingAndSerum(
			TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException;

}
