package com.param.lis.unit.dao;

import com.param.entity.lis.unit.LanguageMaster;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.LanguageMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ILanguageMasterDao extends IGenericDao<LanguageMaster, Integer>
{

	Response addLanguage(LanguageMasterDto languageMasterDto)throws ApplicationException;

	Response getLanguagebyId(Integer orgId, Integer unitId, Integer languagerId) throws ApplicationException;

	Response updateLanguageMaster(LanguageMasterDto languageMasterDto)throws ApplicationException;

	Response activeInactiveLanguage(Integer orgId, Integer languageId, Character languageStatus)throws ApplicationException;

	Response listLanguageMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)throws ApplicationException;

	Response listAllLanguageMaster(Integer orgId, Integer unitId)throws ApplicationException;

	Response getTotalLanguageMasterRecord(Integer orgId, Integer unitId)throws ApplicationException;

	Response<LanguageMasterDto, Integer> getTotalLanguageMasterRecordActive(Integer orgId, Integer unitId)throws ApplicationException;
	}
