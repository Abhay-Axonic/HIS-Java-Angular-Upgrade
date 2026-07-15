package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.BedsideLabInvestigationDto;

import in.param.exception.ApplicationException;
@Transactional
public interface IBedsideLabInvestigationService {

	public Response saveBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException;
	
	public Response getBedsideLabInvestigationByName(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException;
	
	public Response updateBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException;

	public Response updateBedsideLabInvestigationStatus(BedsideLabInvestigationDto bedsideLabInvestigationDto)throws ApplicationException;

	public Response getBedsideLabInvestigationById(Integer bedsideLabId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getBedsideLabInvestigationList(BedsideLabInvestigationDto bedsideLabInvestigationDto)throws ApplicationException;

	public Response getBedsideLabInvestigationCount(BedsideLabInvestigationDto bedsideLabInvestigationDto)throws ApplicationException;

	public Response getActiveBedsideLabInvestigation(Integer organizationId, Integer unitId)throws ApplicationException;
	
	public Response getBedsideLabInvestigationNotId(BedsideLabInvestigationDto bedsideLabInvestigationDto)throws ApplicationException;

		
}
