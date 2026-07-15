package com.param.lis.transaction.service;

import java.util.List;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBiochemValidationWorklistService {

	public Response getValidationWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getValidationWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getValidationWorkListDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException;

	public Response saveValidationWorkList(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException, Exception;
	
	public Response getPrivousResultBySample(BioChemParamDto bioChemParamDto) throws ApplicationException;
	
	public Response labValidationWorklistSearch(SearchDto searchDto) throws ApplicationException;

	public Response consolidatedWorklistSearch(SearchDto searchDto) throws ApplicationException;

	public Response labValidationWorklistTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response consolidatedWorklistTypeSearch(SearchDto searchDto)throws ApplicationException;
}
