package com.param.lis.transaction.dao;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.param.entity.lis.transaction.LabResultMaster;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IReportHandOverDao extends IGenericDao<LabResultMaster, Integer>{

	public Response getReportHandList(BioChemParamDto bioChemParamDto) throws ApplicationException;

	public Response getReportHandListCount(BioChemParamDto bioChemParamDto) throws ApplicationException;
	
	public Response reportHandOverListSearch(@RequestBody SearchDto searchDto) throws ApplicationException;

	public Response updateLabsampDetails(List<LabResultMasterDto> list)throws ApplicationException;

	public Response saveReportHandOverImage(LabResultMasterDto dto);

	public Response reportHandOverListTypeSearch(SearchDto searchDto)throws ApplicationException;

	public Response bulkEmailWorklistPatientSearch(SearchDto searchDto)throws ApplicationException;

	public Response getReportHandOverUrl(List<Integer> labSampleDtlsIdsList, Integer orgUnitId, Integer orgId);
	
}
