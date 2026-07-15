package com.param.lis.global.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.lis.global.dao.ITemplateServiceMapperDao;
import com.param.lis.global.dto.TemplateServiceMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemplateServiceMapperServiceImpl  implements ITemplateServiceMapperService, ICommonConstants, IError{

	@Autowired
	ITemplateServiceMapperDao iTemplateServiceMapperDao;
	
	public Response saveTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException {
		try
		{
		boolean flag=false;
			Iterator ite=templateServiceMapperDto.getSelectTemplateServiceMapperList().iterator(); 
			while (ite.hasNext()) {
				TemplateServiceMapperDto dto = new TemplateServiceMapperDto();
				Integer templateId = (Integer) ite.next();
				
				dto.setUpdatedDate(templateServiceMapperDto.getUpdatedDate());
				dto.setTemplateId(templateId);
				dto.setSpecialityId(templateServiceMapperDto.getSpecialityId());
				dto.setSubSpecialityId(templateServiceMapperDto.getSubSpecialityId());
				dto.setCreatedBy(templateServiceMapperDto.getCreatedBy());
				dto.setOrganizationId(templateServiceMapperDto.getOrganizationId());
				dto.setUnitId(templateServiceMapperDto.getUnitId());
				dto.setCreatedDate(templateServiceMapperDto.getCreatedDate());
				dto.setStatus(templateServiceMapperDto.getStatus());
				dto.setIsDeleted(templateServiceMapperDto.getIsDeleted());
				dto.setServiceId(templateServiceMapperDto.getServiceId());
				dto.setUpdatedBy(templateServiceMapperDto.getUpdatedBy());

				Response res = iTemplateServiceMapperDao.checkTemplateServiceMapperAvailable(dto);
				if(res.getListObject() != null && res.getListObject().size() > 0){
					flag=true;
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
					
				}
				
				
			}
				if(!flag){
					Iterator ite2=templateServiceMapperDto.getSelectTemplateServiceMapperList().iterator(); 
					while (ite2.hasNext()) {
						TemplateServiceMapperDto dto = new TemplateServiceMapperDto();
						Integer templateId = (Integer) ite2.next();
						
						dto.setUpdatedDate(templateServiceMapperDto.getUpdatedDate());
						dto.setTemplateId(templateId);
						dto.setSpecialityId(templateServiceMapperDto.getSpecialityId());
						dto.setSubSpecialityId(templateServiceMapperDto.getSubSpecialityId());
						dto.setCreatedBy(templateServiceMapperDto.getCreatedBy());
						dto.setOrganizationId(templateServiceMapperDto.getOrganizationId());
						dto.setUnitId(templateServiceMapperDto.getUnitId());
						dto.setCreatedDate(templateServiceMapperDto.getCreatedDate());
						dto.setStatus(templateServiceMapperDto.getStatus());
						dto.setIsDeleted(templateServiceMapperDto.getIsDeleted());
						dto.setServiceId(templateServiceMapperDto.getServiceId());
						dto.setUpdatedBy(templateServiceMapperDto.getUpdatedBy());			
						iTemplateServiceMapperDao.saveTemplateServiceMapper(dto);
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	public Response updateTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException {
		try
		{
			List<TemplateServiceMapperDto> listTemplateServiceMapperDto = new ArrayList();
			Iterator ite=templateServiceMapperDto.getSelectTemplateServiceMapperList().iterator(); 
			while (ite.hasNext()) {
				TemplateServiceMapperDto dto=new TemplateServiceMapperDto();

				Integer templateId = (Integer) ite.next();
				dto.setUpdatedDate(templateServiceMapperDto.getUpdatedDate());
				dto.setTemplateId(templateId);
				dto.setSpecialityId(templateServiceMapperDto.getSpecialityId());
				dto.setSubSpecialityId(templateServiceMapperDto.getSubSpecialityId());
				dto.setCreatedBy(templateServiceMapperDto.getCreatedBy());
				dto.setOrganizationId(templateServiceMapperDto.getOrganizationId());
				dto.setUnitId(templateServiceMapperDto.getUnitId());
				dto.setCreatedDate(templateServiceMapperDto.getCreatedDate());
				dto.setStatus(templateServiceMapperDto.getStatus());
				dto.setIsDeleted(templateServiceMapperDto.getIsDeleted());
				dto.setServiceId(templateServiceMapperDto.getServiceId());
				dto.setUpdatedBy(templateServiceMapperDto.getUpdatedBy());
				listTemplateServiceMapperDto.add(dto);
			}
			if(!listTemplateServiceMapperDto.isEmpty())
			{
				return iTemplateServiceMapperDao.updateTemplateServiceMapper(listTemplateServiceMapperDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			} catch (Exception e)
		{
			
			throw e;
		}
	}
	@Override
	public Response updateTemplateServiceMapperStatus(Integer serviceId, Integer organizationId, Integer unitId,
			Character status) throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.updateTemplateServiceMapperStatus(serviceId, organizationId, unitId, status);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response getTemplateServiceMapperlist(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.getTemplateServiceMapperlist(templateServiceMapperDto);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response getTemplateServiceMapperCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.getTemplateServiceMapperCount(organizationId,unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response getActiveTemplateServiceMapper(Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.getActiveTemplateServiceMapper(organizationId, unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response getTemplateServiceMapperById(Integer serviceId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.getTemplateServiceMapperById(serviceId, organizationId, unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response checkTemplateServiceMapperAvailable(TemplateServiceMapperDto templateServiceMapperDto)
			throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.checkTemplateServiceMapperAvailable(templateServiceMapperDto);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	public Response getDetailedServiceList(Integer serviceId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTemplateServiceMapperDao.getDetailedServiceList(serviceId, organizationId, unitId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}
}
