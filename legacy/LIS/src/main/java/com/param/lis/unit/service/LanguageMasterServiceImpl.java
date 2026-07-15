package com.param.lis.unit.service;

import com.param.lis.global.dto.GlobalCommonDto;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.ILanguageMasterDao;
import com.param.lis.unit.dto.LanguageMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class LanguageMasterServiceImpl implements ILanguageMasterService, ICommonConstants, IError
{

	@Autowired
	ILanguageMasterDao iLanguageMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response addLanguage(LanguageMasterDto languageMasterDto) throws ApplicationException
	{
		

		try {
			LanguageMasterDto departmentMat = (LanguageMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LANGUAGE_BY_CODE")
					.setParameter("code", languageMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", languageMasterDto.getOrgId())
				//	.setParameter("unitId", languageMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class)).uniqueResult();
			if (departmentMat == null) {
				languageMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
				Response response = iLanguageMasterDao.addLanguage(languageMasterDto);
				return response;
			} else {
				return new Response(ERROR, ERR_500, HEADER_CODE_EXISIS, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
	}
	 
	@Override
	@Transactional
	public Response getLanguagebyId(Integer orgId,Integer unitId, Integer languagerId) throws ApplicationException
	{
		try
		{
			return iLanguageMasterDao.getLanguagebyId(orgId,unitId,languagerId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateLanguageMaster(LanguageMasterDto languageMasterDto) throws ApplicationException
	{
		
		try
		{
			LanguageMasterDto departmentMat = (LanguageMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_LANGUAGE_BY_CODE")
					.setParameter("code", languageMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", languageMasterDto.getOrgId())
//					.setParameter("unitId", languageMasterDto.getUnitId())
					.setParameter("languageId", languageMasterDto.getLanguageId())
					.setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				languageMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
				return iLanguageMasterDao.updateLanguageMaster(languageMasterDto);
			} else
			{
				return new Response(ERROR, ERR_500, LANGUAGE_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	
	}

	@Override
	@Transactional
	public Response activeInactiveLanguage(Integer orgId, Integer languageId, Character languageStatus)
			throws ApplicationException
	{
		try
		{
			return iLanguageMasterDao.activeInactiveLanguage(orgId, languageId, languageStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listLanguageMaster(Integer orgId,Integer unitId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iLanguageMasterDao.listLanguageMaster(orgId, unitId,offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	public Response<GlobalCommonDto, Integer> listAllLanguageMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			return iLanguageMasterDao.listAllLanguageMaster(orgId, unitId);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalLanguageMasterRecord(Integer orgId,Integer unitId) throws ApplicationException
	{
		try
		{
			return iLanguageMasterDao.getTotalLanguageMasterRecord(orgId,unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalLanguageMasterRecordActive(Integer orgId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iLanguageMasterDao.getTotalLanguageMasterRecordActive(orgId,unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
