package com.param.lis.transaction.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.param.entity.lis.transaction.LabResultMaster;
import com.param.global.dto.TemplateTransactionDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;


@SuppressWarnings("rawtypes")
public interface IBioChemResultEntryDao extends IGenericDao<LabResultMaster, Integer>
{
	public Response getResultEntryList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getResultEntryListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getResultEntryDetails(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response saveEntryDetails(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException;
	
	public Response getSingleParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getMultyParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException;
	
	
	public Response getMultyParameterTest(BioChemParamDto bioChemParamDto) throws ApplicationException;
	
	
	public Response getTestMachineResult(Map<String,Integer> sampleIds) throws ApplicationException;
	  
	public Response labResultEntrySearch(SearchDto searchDto) throws ApplicationException;
	
	
	//change
	public Response getMultyParameterTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException;
	public Response getMultyParameterTestDetails(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response saveReleaseEntryDetails(LabResultMasterDto labResultMasterDto) throws ApplicationException;

	public Response consolidatedResultEntrySearch(SearchDto searchDto) throws ApplicationException;

	public Response consolidatedResultEntryTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response labResultEntryTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response getParameterValueByMachineId(BioChemParamDto bioChemParamDto)throws ApplicationException;

	public Response checkLabResultIsExistAndUpdateStatus(List<LabResultMasterDto> listLabResultMasterDto)throws ApplicationException;
	
    public Response<LabResultMasterDto, Integer> updateEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)throws ApplicationException;

	public Response<LabSampleMasterDto, Integer> barcodeScannerListSearch(SearchDto searchDto);

	public Response<LabResultMasterDto, Integer> barcodeScannerListSave(
			List<LabResultMasterDto> listLabResultMasterDto);

	public Response<LabSampleMasterDto, Integer> getSampleRecollection(SearchDto searchDto);

	public Response<LabResultMasterDto, Integer> saveInterfaceResult(LabResultMasterDto labResultMasterDto);

    public List<Integer> getLabSampleDetailIdsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) throws ApplicationException;

    public List<Integer> getLabSampleDetailIdsByMobileNo(LabResultMasterDto labResultMasterDto) throws ApplicationException;

    public List<TemplateTransactionDto> getRISReportsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) throws ApplicationException;
}
