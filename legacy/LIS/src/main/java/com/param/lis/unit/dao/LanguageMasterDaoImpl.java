package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.LanguageMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.LanguageMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class LanguageMasterDaoImpl extends GenericDao<LanguageMaster, Integer>
		implements ILanguageMasterDao, ICommonConstants, IError
{

	public LanguageMasterDaoImpl()
	{
		super(LanguageMaster.class);
	}

	 @Autowired
	 private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(LanguageMaster.class);

	@Override
	public Response addLanguage(LanguageMasterDto languageMasterDto) throws ApplicationException
	{
		try
		{
			LanguageMaster languageMaster = new LanguageMaster();
			languageMaster.setCode(languageMasterDto.getCode());
			languageMaster.setDesc(languageMasterDto.getDesc());
			languageMaster.setStatus(languageMasterDto.getStatus());
			languageMaster.setCreatedBy(languageMasterDto.getCreatedBy());
			languageMaster.setCreatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
			languageMaster.setUpdatedBy(languageMasterDto.getUpdatedBy());
			languageMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
			languageMaster.setOrgId(languageMasterDto.getOrgId());
		    languageMaster.setUnitId(languageMasterDto.getUnitId());
		    sessionFactory.getCurrentSession().save(languageMaster);
		    
			return new Response(SUCCESS, SUCCESS_200, LANGUAGE_ADD_SUCC, null, null);
			
		}catch(HibernateException er){
			logger.error("Exection", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
	}

	@Override
	public Response getLanguagebyId(Integer orgId,Integer unitId,Integer languageId) throws ApplicationException
	{
		
		try
		{
			LanguageMasterDto departmentMaster = (LanguageMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LANGUAGE_BY_LANGUAGE_ID")
					.setParameter("orgId", orgId)
					.setParameter("languageId", languageId)
					.setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class))
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, departmentMaster);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
		
	}

	@Override
	public Response updateLanguageMaster(LanguageMasterDto languageMasterDto) throws ApplicationException
	{
		try
		{
			LanguageMaster languageMaster = findById(languageMasterDto.getLanguageId());
			languageMaster.setLanguageId(languageMaster.getLanguageId());
			languageMaster.setCode(languageMasterDto.getCode());
			languageMaster.setDesc(languageMasterDto.getDesc());
			languageMaster.setStatus(languageMasterDto.getStatus());
			languageMaster.setCreatedBy(languageMasterDto.getCreatedBy());
			languageMaster.setCreatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
			languageMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(languageMasterDto.getUnitId()));
			languageMaster.setUpdatedBy(languageMasterDto.getUpdatedBy());
			languageMaster.setOrgId(languageMasterDto.getOrgId());
		    languageMaster.setUnitId(languageMasterDto.getUnitId());
		    sessionFactory.getCurrentSession().update(languageMaster);
		    
			return new Response(SUCCESS, SUCCESS_200, LANGUAGE_UPDATE_SUCC, null, null);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}


	
	@Override
	public Response activeInactiveLanguage(Integer orgId, Integer languageId, Character languageStatus)
			throws ApplicationException
	{
		try
		{
			LanguageMaster languageMaster = findById(languageId);
			if (languageMaster.getLanguageId() != 0)
			{
				languageMaster.setStatus(languageStatus);
				LanguageMaster departmentMst = update(languageMaster);
				if (departmentMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200, LANGUAGE_ACTIVATE_SUCC, null, null);
				} else if (departmentMst.getStatus() == INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200, LANGUAGE_INACTIVATE_SUCC, null, null);
				} else
				{
					if (languageStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200, LANGUAGE_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200, LANGUAGE_INACTIVATE_FAIL, null, null);
					}
				}
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, LANGUAGE_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response listLanguageMaster(Integer orgId,Integer unitId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			List<LanguageMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_LANGUAGE_MASTER_LIST")
					.setParameter("orgId", orgId)
//					.setInteger("unitId", unitId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class)).list();
			
			if(listTechniqueMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, LANGUAGE_RECORDS_NOT_FOUND, null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listTechniqueMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response listAllLanguageMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<LanguageMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_LANGUAGE_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class)).list();

			if(listTechniqueMasterDto.isEmpty()){
				return new Response(ERROR, ERR_500, LANGUAGE_RECORDS_NOT_FOUND, null, null);
			}else{
				return new Response(SUCCESS, SUCCESS_200, null, listTechniqueMasterDto, null);
			}

		} catch (Exception e){
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTotalLanguageMasterRecord(Integer orgId,Integer unitId) throws ApplicationException
	{
		try
		{
			BigInteger languageMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_LANGUAGE_RECORD")
//					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.uniqueResult();
		
			if (languageMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, languageMasterTotalRecordCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTotalLanguageMasterRecordActive(Integer orgId, Integer unitId)
			throws ApplicationException {try
			{
				
				List<LanguageMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TOTAL_LANGUAGE_RECORD_ACTIVE")
						.setParameter("orgId", orgId)
//						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(LanguageMasterDto.class)).list();
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTechniqueMasterDto, null);
				
				

			} catch (Exception e)
			{
				logger.error("Exection", e);
				throw e;
			}}

}
