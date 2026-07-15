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
import com.param.lis.global.dao.IContainerMasterDao;
import com.param.lis.global.dto.ContainerMasterDto;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class ContainerMasterServiceImpl implements IContainerMasterService, ICommonConstants, IError
{

	@Autowired
	IContainerMasterDao iContainerMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response addContainerMaster(ContainerMasterDto containerMasterDto) throws ApplicationException
	{
		try
		{
			List<GlobalCommonDto> containerMstDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTAINER_BY_CODE")
					.setParameter("code", containerMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", containerMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			if (containerMstDto.size() == 0)
			{
				
				List<GlobalCommonDto> containerMstDto1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CONTAINER_BY_DESC")
						.setParameter("desc", containerMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", containerMasterDto.getOrgId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
				if (containerMstDto1.size() == 0)
				{
					containerMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					Response response = iContainerMasterDao.addContainerMaster(containerMasterDto);
					return response;
				
				} else
				{
					return new Response(ERROR, ERR_500, CONTAINER_DESC_EXISIS, null, null);
				}
			} else
			{
				return new Response(ERROR, ERR_500, CONTAINER_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getContainerMasterById(Integer orgId, Integer containerId) throws ApplicationException
	{
		try
		{
			return iContainerMasterDao.getContainerMasterById(orgId, containerId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateContainerMaster(ContainerMasterDto containerMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto containerMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_CONTAINER_BY_CODE")
					.setString("code", containerMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", containerMasterDto.getOrgId())
					.setParameter("containerId", containerMasterDto.getContainerId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (containerMat == null)
			{
				GlobalCommonDto containerMat1 = (GlobalCommonDto) sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_GET_CONTAINER_BY_DESC")
						.setParameter("desc", containerMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", containerMasterDto.getOrgId())
						.setParameter("containerId", containerMasterDto.getContainerId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
				if (containerMat1 == null)
				{
					containerMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					return iContainerMasterDao.updateContainerMaster(containerMasterDto);
				
				} else
				{
					return new Response(ERROR, ERR_500, CONTAINER_DESC_EXISIS, null, null);
				}

			} else
			{
				return new Response(ERROR, ERR_500, CONTAINER_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@Override
	@Transactional
	public Response  ActivateInactivateContainerMaster(Integer orgId, Integer sampleId, Character containerStatus)
			throws ApplicationException
	{
		try
		{
			return iContainerMasterDao.ActivateInactivateContainerMaster(orgId, sampleId, containerStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listContainerMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iContainerMasterDao.listContainerMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalContainerMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iContainerMasterDao.getToTalContainerMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
