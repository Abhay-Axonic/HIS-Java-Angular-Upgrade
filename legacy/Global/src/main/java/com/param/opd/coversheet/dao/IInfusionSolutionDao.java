package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.InfusionAdditiveDto;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IInfusionSolutionDao {

	public Response saveInfusionSolution(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	
	public Response getInfusionSolutionItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	
	public Response getInfusionAdditiveItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException; 
	
	public Response saveInfusionAdditive(InfusionAdditiveDto infusionAdditiveDto)  throws ApplicationException;
	
	public Response getListOfInfusion(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;
	public Response getAdditiveListById(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException;

	public Response updateInfusionStopFlag(InfusionSolutionDto infusionSolutionDto)throws ApplicationException;

	public List<PrescriptionDetailsDto> getPresDetIdAndPresId(Integer infusionSolutionId);

	public PrescriptionDetailsDto updatePresDetIdAndPresId(PrescriptionDetailsDto prescriptionDetailsDto);


}
