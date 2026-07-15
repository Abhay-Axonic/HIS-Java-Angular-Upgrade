package com.param.lis.global.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IPrerequisitesDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.PrerequisitesMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class PrerequisitesMasterServiceImpl implements IPrerequisitesService, ICommonConstants, IError
{

	@Autowired
	IPrerequisitesDao iPrerequisitesDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response addPrerequisitesMaster(PrerequisitesMasterDto prerequisitesMasterDto) throws ApplicationException
	{
		try
		{
			List<GlobalCommonDto> containerMstDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREREQUISITES_BY_CODE")
					.setString("code", prerequisitesMasterDto.getCode().trim().toLowerCase())
					.setInteger("orgId", prerequisitesMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			if (containerMstDto.size() == 0)
			{
				List<GlobalCommonDto> containerMstDto1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PREREQUISITES_BY_DESC")
						.setString("desc", prerequisitesMasterDto.getDesc().trim().toLowerCase())
						.setInteger("orgId", prerequisitesMasterDto.getOrgId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
				if (containerMstDto1.size() == 0)
				{
					prerequisitesMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					Response response = iPrerequisitesDao.addPrerequisitesMaster(prerequisitesMasterDto);
					return response;
				
				} else
				{
					return new Response(ERROR, ERR_500, PREREQUISITES_DESC_EXISIS, null, null);
				}
			} else
			{
				return new Response(ERROR, ERR_500, PREREQUISITES_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPrerequisitesMasterById(Integer orgId, Integer prerequisitesId) throws ApplicationException
	{
		try
		{
			return iPrerequisitesDao.getPrerequisitesMasterById(orgId, prerequisitesId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePrerequisitesMaster(PrerequisitesMasterDto prerequisitesMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto prerequisitesMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_PREREQUISITES_BY_CODE")
					.setString("code", prerequisitesMasterDto.getCode().trim().toLowerCase())
					.setInteger("orgId", prerequisitesMasterDto.getOrgId())
					.setInteger("prerequisitesId", prerequisitesMasterDto.getPrerequisitesId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (prerequisitesMat == null)
			{
				
				GlobalCommonDto prerequisitesMat1 = (GlobalCommonDto) sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_GET_PREREQUISITES_BY_DESC")
						.setString("desc", prerequisitesMasterDto.getDesc().trim().toLowerCase())
						.setInteger("orgId", prerequisitesMasterDto.getOrgId())
						.setInteger("prerequisitesId", prerequisitesMasterDto.getPrerequisitesId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
				if (prerequisitesMat1 == null)
				{
					prerequisitesMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					return iPrerequisitesDao.updatePrerequisitesMaster(prerequisitesMasterDto);
				
				} else
				{
					return new Response(ERROR, ERR_500, PREREQUISITES_DESC_EXISIS, null, null);
				}

			} else
			{
				return new Response(ERROR, ERR_500, PREREQUISITES_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
		/*try
		{
			prerequisitesMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
			return iPrerequisitesDao.updatePrerequisitesMaster(prerequisitesMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}*/
	}

	@Override
	@Transactional
	public Response ActivateInactivatePrerequisitesMaster(Integer orgId, Integer prerequisitesId, Character prerequisitesStatus)
			throws ApplicationException
	{
		try
		{
			return iPrerequisitesDao.ActivateInactivatePrerequisitesMaster(orgId, prerequisitesId, prerequisitesStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listPrerequisitesMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iPrerequisitesDao.listPrerequisitesMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalPrerequisitesMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iPrerequisitesDao.getToTalPrerequisitesMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
