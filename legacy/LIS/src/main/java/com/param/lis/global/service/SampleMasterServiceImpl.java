package com.param.lis.global.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.SampleMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ISampleMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.SampleMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class SampleMasterServiceImpl implements ISampleMasterService, ICommonConstants, IError
{

	@Autowired
	ISampleMasterDao iSampleMasterDao;
	
	@Autowired
	IEmrService EmrService;   

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response addSampleMaster(SampleMasterDto sampleMasterDto) throws ApplicationException
	{
		try
		{
			List<GlobalCommonDto> sampleMstDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SAMPLE_BY_CODE")
					.setParameter("code", sampleMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", sampleMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			if (sampleMstDto.size() == 0)
			{
				List<GlobalCommonDto> sampleMstDto1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SAMPLE_BY_DESC")
						.setParameter("desc", sampleMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", sampleMasterDto.getOrgId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
				if (sampleMstDto1.size() == 0)
				{
				
					sampleMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					Response response = iSampleMasterDao.addSampleMaster(sampleMasterDto);
					if(response.getObject()!=null) {
						SampleMaster dto = (SampleMaster)response.getObject();
						sampleMasterDto.setSampleId(dto.getSampleId());
						Request request = new Request();
						Payload payload = new Payload();
						payload.setActionType("create");
						payload.setData(sampleMasterDto);
						request.setKey(MasterKeyConstants.SampleMasterType.toString());
						request.setPayload(payload);
						//EmrService.publish(request);
						
					}
					
					return response;
				
				} else
				{
					return new Response(ERROR, ERR_500, SAMPLE_DESC_EXISIS, null, null);
				}
			} else
			{
				return new Response(ERROR, ERR_500, SAMPLE_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSampleMasterById(Integer orgId, Integer sampleId) throws ApplicationException
	{
		try
		{
			return iSampleMasterDao.getSampleMasterById(orgId, sampleId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateSampleMaster(SampleMasterDto sampleMasterDto) throws ApplicationException
	{
		try
		{
			GlobalCommonDto departmentMat = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_SAMPLE_BY_CODE")
					.setParameter("code", sampleMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", sampleMasterDto.getOrgId())
					.setParameter("sampleId", sampleMasterDto.getSampleId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
			if (departmentMat == null)
			{
				GlobalCommonDto departmentMat1 = (GlobalCommonDto) sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_GET_SAMPLE_BY_DESC")
						.setParameter("desc", sampleMasterDto.getDesc().trim().toLowerCase())
						.setParameter("orgId", sampleMasterDto.getOrgId())
						.setParameter("sampleId", sampleMasterDto.getSampleId())
						.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();
				if (departmentMat1 == null)
				{
					sampleMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
					return iSampleMasterDao.updateSampleMaster(sampleMasterDto);
				
				} else
				{
					return new Response(ERROR, ERR_500, SAMPLE_DESC_EXISIS, null, null);
				}

			} else
			{
				return new Response(ERROR, ERR_500, SAMPLE_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@Override
	@Transactional
	public Response ActivateInactivateSampleMaster(Integer orgId, Integer sampleId, Character sampleStatus)
			throws ApplicationException
	{
		try
		{
			return iSampleMasterDao.ActivateInactivateSampleMaster(orgId, sampleId, sampleStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listSampleMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException
	{
		try
		{
			return iSampleMasterDao.listSampleMaster(orgId, offset, recordPerPage);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getToTalSampleMasterRecord(Integer orgId) throws ApplicationException
	{
		try
		{
			return iSampleMasterDao.getToTalSampleMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

}
