package com.param.lis.serology.transaction.dao;

import java.util.List;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.serology.dto.SerologyResultTestMapperDto;
import com.param.lis.serology.dto.SerologyTestResultDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISerologyResultDao 
{
	  public Response getSerologyResultEntryList(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response getSerologyResultEntryListCount(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response searchSerologyResultEntryList(SearchDto dto) throws ApplicationException;
	  public Response saveResultEntry(SerologyTestResultDto dto) throws ApplicationException;
	  public Response serologyResultTestMapper(SerologyResultTestMapperDto dto) throws ApplicationException;
	  
	  public Response getSerologyReportEntryDetails(List<Integer> labSampleDtlsId) throws ApplicationException;
	  public Response getSerologyValidationList(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response getSerologyValidationListCount(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response searchSerologyValidationList(SearchDto dto) throws ApplicationException;
	  public Response saveValidation(SerologyTestResultDto dto) throws ApplicationException;
	  
	  public Response getSerologyReportValidationDetails(List<Integer> labSampleDtlsId) throws ApplicationException;
	  public Response getSerologyReportReleaseList(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response getSerologyReportReleaseCount(MicrobioParamDto microbioParamDto) throws ApplicationException;
	  public Response searchSerologyReportReleaseList(SearchDto dto) throws ApplicationException;
	  public Response saveReportRelease(SerologyTestResultDto dto) throws ApplicationException;
	  public Response updateReportRelease(SerologyResultTestMapperDto mapperDto)throws ApplicationException;
	  public Response ResultEntryOrReleaseEntry(SerologyTestResultDto dto)throws ApplicationException;
	  public Response serologyResultTestMapperAndRelease(SerologyResultTestMapperDto mapperDto)throws ApplicationException;
		public Response updateResultEntry(SerologyTestResultDto dto)throws ApplicationException;
		public Response updateSerologyResultTestMapper(SerologyResultTestMapperDto mapperDto)throws ApplicationException;
		public Response recollectSampleForSerlogy(SearchDto dto)throws ApplicationException;
		public Response retestSampleForSerlogy(SearchDto dto)throws ApplicationException;
		public Response updateAmend(List<SerologyTestResultDto> list)throws ApplicationException;
	  
}
