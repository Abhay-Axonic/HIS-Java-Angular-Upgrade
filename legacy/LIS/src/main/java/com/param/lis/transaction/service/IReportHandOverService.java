package com.param.lis.transaction.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IReportHandOverService{

	public Response getReportHandList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportHandListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response reportHandOverListSearch(@RequestBody SearchDto searchDto) throws ApplicationException;

	public Response updateLabsampDetails(List<LabResultMasterDto> list)throws ApplicationException;

	public Response saveReportHandOverImage(LabResultMasterDto dto)throws ApplicationException;

	public Response sendReportOnMail(List<LabResultMasterDto> list, HttpServletRequest request)throws ApplicationException, Exception;

	public Response reportHandOverListTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response bulkEmailWorklistPatientSearch(SearchDto searchDto)throws ApplicationException;

	public Response sendBulkEmailToCompanyOrInsurance(List<SmsUtilityConfigInfoDto> dtoList)throws ApplicationException, Exception;
}
