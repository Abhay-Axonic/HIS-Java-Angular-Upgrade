package com.param.lis.transaction.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.param.global.dto.TemplateTransactionDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBioChemResultEntryService
{
	public Response getResultEntryList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getResultEntryListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getResultEntryDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException;

	public Response saveEntryDetails(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException;
	
	public Response getSingleParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getMultyParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException;
	
	public Response getTestMachineResult(Map<String,Integer> sampleIds) throws ApplicationException;
	
	public Response labResultEntrySearch(SearchDto searchDto) throws ApplicationException;

	public Response saveReleaseEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)throws ApplicationException;

	public Response sendEmail(List<LabResultMasterDto> list)throws ApplicationException, Exception;

	public Response consolidatedResultEntrySearch(SearchDto searchDto)throws ApplicationException;

	public Response consolidatedResultEntryTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response labResultEntryTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response getParameterValueByMachineId(BioChemParamDto bioChemParamDtoList)throws ApplicationException;

	public Response<LabResultMasterDto, Integer> updateEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)throws ApplicationException;

	public Response<LabSampleMasterDto, Integer> barcodeScannerListSearch(SearchDto searchDto);

	public Response<LabResultMasterDto, Integer> barcodeScannerListSave(
			List<LabResultMasterDto> listLabResultMasterDto);

	public Response<LabSampleMasterDto, Integer> getSampleRecollection(SearchDto searchDto);

	public Response<LabResultMasterDto, Integer> saveInterfaceResult(LabResultMasterDto labResultMasterDto);


    public List<Integer> getLabSampleDetailIdsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) throws ApplicationException;

    public Response getLabSampleDetailIdsByMobileNo(LabResultMasterDto labResultMasterDto) throws ApplicationException;

    public List<TemplateTransactionDto> getRISReportsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) throws ApplicationException;
}
