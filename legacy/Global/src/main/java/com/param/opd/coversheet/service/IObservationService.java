package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.ObservationDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IObservationService {
	public Response saveObservation(ObservationDto observationDto) throws ApplicationException;
	
	public Response updateObservation(ObservationDto observationDto) throws ApplicationException;
	
	public Response updateObservationStatus(ObservationDto observationDto) throws ApplicationException;
	
	public Response getObservationList(ObservationDto observationDto) throws ApplicationException;
	
	public Response getObservationById(Integer observationId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getObservationCount(ObservationDto observationDto) throws ApplicationException;
}
