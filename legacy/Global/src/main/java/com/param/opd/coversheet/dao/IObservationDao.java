package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.ObservationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IObservationDao {
	
	public Response saveObservation(ObservationDto observationDto) throws ApplicationException;
	
	public Response updateObservation(ObservationDto observationDto) throws ApplicationException;
	
	public Response updateObservationStatus(ObservationDto observationDto) throws ApplicationException;
	
	public Response getObservationList(ObservationDto observationDto) throws ApplicationException;
	
	public Response getObservationById(Integer observationId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getObservationCount(ObservationDto observationDto) throws ApplicationException;

	public Response savefPatientObservation(List<ObservationDto> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

	public Response updateCovTempPatientObservation(List<ObservationDto> list, CoversheetTemplateMasterDto coversheetTemplateMasterDto);
}
