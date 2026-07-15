package com.param.global.unit.service;


import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.ITemplateDiagnosisMapperDao;
import com.param.global.unit.dto.TemplateDiagnosisMapperDto;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemplateDiagnosisMapperServiceImpl implements ITemplateDiagnosisMapperService, ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TemplateDiagnosisMapperServiceImpl.class);

	@Autowired
	ITemplateDiagnosisMapperDao iTemplateDiagnosisMapperDao;
	@Override
	public Response saveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try
		{
			Response res = iTemplateDiagnosisMapperDao.checkActiveTemplateDiagnosisMapperAvaiable(templateDiagnosisMapperDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
			}else{
			Iterator ite=templateDiagnosisMapperDto.getSelectTemplateDiagnosisMapperList().iterator(); 
			while (ite.hasNext()) {
				TemplateDiagnosisMapperDto templateDiagnosisMapper = new TemplateDiagnosisMapperDto();
				Integer diagnosisId = (Integer) ite.next();
//				String date = TimeZoneComponent.getDateByzone(unitId).toString();
				templateDiagnosisMapper.setCreatedDate(templateDiagnosisMapperDto.getCreatedDate());
				templateDiagnosisMapper.setTemplateId(templateDiagnosisMapperDto.getTemplateId());
				templateDiagnosisMapper.setDiagnosisId(diagnosisId);
				templateDiagnosisMapper.setStatus(templateDiagnosisMapperDto.getStatus());
				templateDiagnosisMapper.setOrganisationId(templateDiagnosisMapperDto.getOrganisationId());
				templateDiagnosisMapper.setUnitId(templateDiagnosisMapperDto.getUnitId());
				templateDiagnosisMapper.setCreatedBy(templateDiagnosisMapperDto.getCreatedBy());
				templateDiagnosisMapper.setUpdatedBy(templateDiagnosisMapperDto.getUpdatedBy());
				templateDiagnosisMapper.setUpdatedDate(templateDiagnosisMapperDto.getUpdatedDate());
				templateDiagnosisMapper.setIsDeleted(templateDiagnosisMapperDto.getIsDeleted());

				iTemplateDiagnosisMapperDao.saveTemplateDiagnosisMapper(templateDiagnosisMapper);
			}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("saveTemplateDiagnosisMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response updateTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)throws ApplicationException {
		try
		{
			if(!(templateDiagnosisMapperDto.getTemplateId().equals(templateDiagnosisMapperDto.getPrevTemplateId()))){
			Response res = iTemplateDiagnosisMapperDao.checkActiveTemplateDiagnosisMapperAvaiable(templateDiagnosisMapperDto);
				if(res.getListObject() != null && res.getListObject().size() > 0){
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}
			}
			List<TemplateDiagnosisMapperDto> listTemplateDiagnosisMapperDto = new ArrayList();
			Iterator ite=templateDiagnosisMapperDto.getSelectTemplateDiagnosisMapperList().iterator(); 
			while (ite.hasNext()) {
				TemplateDiagnosisMapperDto templateDiagnosisMapperDto1=new TemplateDiagnosisMapperDto();
				TemplateDiagnosisMapperDto templateDiagnosisMapper = new TemplateDiagnosisMapperDto();
				Integer diagnosisId = (Integer) ite.next();
//				String date = TimeZoneComponent.getDateByzone(unitId).toString();
				templateDiagnosisMapperDto1.setUpdatedDate(templateDiagnosisMapperDto.getUpdatedDate());
				templateDiagnosisMapper.setTemplateId(templateDiagnosisMapperDto.getTemplateId());
				templateDiagnosisMapperDto1.setCreatedBy(templateDiagnosisMapperDto.getCreatedBy());
				templateDiagnosisMapperDto1.setOrganisationId(templateDiagnosisMapperDto.getOrganisationId());
				templateDiagnosisMapperDto1.setUnitId(templateDiagnosisMapperDto.getUnitId());
				templateDiagnosisMapperDto1.setTemplateId(templateDiagnosisMapperDto.getTemplateId());
				templateDiagnosisMapperDto1.setCreatedDate(templateDiagnosisMapperDto.getCreatedDate());
				templateDiagnosisMapperDto1.setStatus(templateDiagnosisMapperDto.getStatus());
				templateDiagnosisMapperDto1.setIsDeleted(templateDiagnosisMapperDto.getIsDeleted());
				templateDiagnosisMapperDto1.setDiagnosisId(diagnosisId);
				templateDiagnosisMapper.setOrganisationId(templateDiagnosisMapperDto.getOrganisationId());
				templateDiagnosisMapperDto1.setUpdatedBy(templateDiagnosisMapperDto.getUpdatedBy());
				templateDiagnosisMapperDto1.setPrevTemplateId(templateDiagnosisMapperDto.getPrevTemplateId());
				listTemplateDiagnosisMapperDto.add(templateDiagnosisMapperDto1);
			}
			if(!listTemplateDiagnosisMapperDto.isEmpty())
			{
				return iTemplateDiagnosisMapperDao.updateTemplateDiagnosisMapper(listTemplateDiagnosisMapperDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		} catch (Exception e)
		{
			logger.error("updateTemplateDiagnosisMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateTemplateDiagnosisMapperStatus(Integer templateId, Integer organisationId, Integer unitId, Character status)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.updateTemplateDiagnosisMapperStatus(templateId, organisationId, unitId, status);
		} catch (Exception e)
		{
			logger.error("updateTemplateDiagnosisMapperStatus failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getTemplateDiagnosisMapperlist(Integer offset, Integer noOfRecordsPerPage)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getTemplateDiagnosisMapperlist(offset, noOfRecordsPerPage);
		} catch (Exception e)
		{
			logger.error("getTemplateDiagnosisMapperlist failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getTemplateDiagnosisMapperById(Integer templateId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getTemplateDiagnosisMapperById(templateId, organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getTemplateDiagnosisMapperById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getActiveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getActiveTemplateDiagnosisMapper(templateDiagnosisMapperDto);
		} catch (Exception e)
		{
			logger.error("getActiveTemplateDiagnosisMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response checkActiveTemplateDiagnosisMapperAvaiable(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.checkActiveTemplateDiagnosisMapperAvaiable(templateDiagnosisMapperDto);
		} catch (Exception e)
		{
			logger.error("checkActiveTemplateDiagnosisMapperAvaiable failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getTemplateDiagnosisMapperDatailedList(Integer templateId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getTemplateDiagnosisMapperDatailedList(templateId, organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getTemplateDiagnosisMapperDatailedList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getTemplateDiagnosisMapperCount()
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getTemplateDiagnosisMapperCount();
		} catch (Exception e)
		{
			logger.error("getTemplateDiagnosisMapperCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getTemplateList(Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getTemplateList(organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getTemplateList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getListOfDiagnosis(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iTemplateDiagnosisMapperDao.getListOfDiagnosis(organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getListOfDiagnosis failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	
}
