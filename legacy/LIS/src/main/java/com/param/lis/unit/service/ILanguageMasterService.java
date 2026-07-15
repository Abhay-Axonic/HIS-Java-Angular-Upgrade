package com.param.lis.unit.service;

import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dto.HeaderMasterDto;
import com.param.lis.unit.dto.LanguageMasterDto;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Transactional
public interface ILanguageMasterService
{

	public Response<LanguageMasterDto, Integer> addLanguage(LanguageMasterDto languageMasterDto) throws ApplicationException;

	public Response<GlobalCommonDto, Integer> getLanguagebyId(Integer orgId, Integer unitId, Integer languageId)throws ApplicationException;

	public Response<LanguageMasterDto, Integer> updateLanguageMaster(LanguageMasterDto languageMasterDto)throws ApplicationException;

	public Response<LanguageMasterDto, Integer> activeInactiveLanguage(Integer orgId, Integer languageId,
			Character languageStatus)throws ApplicationException;

	public Response<GlobalCommonDto, Integer> listLanguageMaster(Integer orgId, Integer unitId, Integer offset,
			Integer recordPerPage)throws ApplicationException;

	public Response<GlobalCommonDto, Integer> listAllLanguageMaster(Integer orgId, Integer unitId)throws ApplicationException;

	public Response<LanguageMasterDto, Integer> getTotalLanguageMasterRecord(Integer orgId, Integer unitId)throws ApplicationException;

	public Response<LanguageMasterDto, Integer> getTotalLanguageMasterRecordActive(Integer orgId, Integer unitId)throws ApplicationException;
	

}
