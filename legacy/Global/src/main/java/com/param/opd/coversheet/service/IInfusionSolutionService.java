package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.InfusionSolutionDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IInfusionSolutionService {
	
	public Response saveInfusionSolution(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	
	public Response getInfusionSolutionItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	
	public Response getInfusionAdditiveItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	
	public Response getListOfInfusion(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	public Response getAdditiveListById(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;

	public Response updateInfusionStopFlag(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
    public Response getPresDtlDataByPresId(Integer integer, Integer organizationId, Integer unitId);

}