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
import com.param.lis.global.dao.ITechinqueMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.TechniqueMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class TechinqueMasterServiceImpl implements ITechinqueMasterService, ICommonConstants, IError
{

	@Autowired
	ITechinqueMasterDao iTechinqueMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public Response addTechinqueMaster(TechniqueMasterDto techniqueMasterDto) throws ApplicationException
	{
		try
		{		
			List<GlobalCommonDto> containerMstDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TECHNIQUE_BY_CODE")
					.setParameter("code", techniqueMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", techniqueMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			if (containerMstDto.size() == 0)
			{
				List<GlobalCommonDto> containerMstDto1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TECHNIQUE_BY_DESC")
						.setParameter("desc", techniqueMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", techniqueMasterDto.getOrgId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
				if (containerMstDto1.size() == 0)
				{
					techniqueMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					Response response = iTechinqueMasterDao.addTechinqueMaster(techniqueMasterDto);
					return response;
				
				} else
				{
					return new Response(ERROR, ERR_500, TECHINQUE_DESC_EXISIS, null, null);
				}	
			} else
			{
				return new Response(ERROR, ERR_500, TECHINQUE_CODE_EXISIS, null, null);
			}		
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTechinqueMasterById(Integer orgId, Integer techniqueId) throws ApplicationException
	{
		try
		{
			return iTechinqueMasterDao.getTechinqueMasterById(orgId, techniqueId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTechinqueMaster(TechniqueMasterDto techniqueMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_TECHNIQUE_BY_CODE")
					.setParameter("code", techniqueMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", techniqueMasterDto.getOrgId())
					.setParameter("techniqueId", techniqueMasterDto.getTechniqueId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				GlobalCommonDto departmentMat1 = (GlobalCommonDto) sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_GET_TECHNIQUE_BY_DESC")
						.setParameter("desc", techniqueMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", techniqueMasterDto.getOrgId())
						.setParameter("techniqueId", techniqueMasterDto.getTechniqueId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
				if (departmentMat1 == null)
				{
					techniqueMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					return iTechinqueMasterDao.updateTechinqueMaster(techniqueMasterDto);
				
				} else
				{
					return new Response(ERROR, ERR_500, TECHINQUE_DESC_EXISIS, null, null);
				}

			} else
			{
				return new Response(ERROR, ERR_500, TECHINQUE_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@Override
	@Transactional
	public Response ActivateInactivateTechinqueMaster(Integer orgId, Integer sampleId, Character sampleStatus)
			throws ApplicationException
	{
		try
		{
			return iTechinqueMasterDao.ActivateInactivateTechinqueMaster(orgId, sampleId, sampleStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listTechinqueMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iTechinqueMasterDao.listTechinqueMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalTechinqueMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iTechinqueMasterDao.getToTalTechinqueMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
