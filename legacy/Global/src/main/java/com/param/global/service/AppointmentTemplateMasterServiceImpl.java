package com.param.global.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.templet.model.AppointmentTemplateMaster;
import com.param.adt.templet.model.AppointmentTemplateMasterMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAppointmentTemplateMasterDao;
import com.param.global.dao.IAppointmentTemplateMasterMapperDao;
import com.param.global.dao.IAppointmentTemplateServiceDao;
import com.param.global.model.AppointmentTemplateMasterDto;
import com.param.global.model.AppointmentTemplateMasterMapperDto;
import com.param.global.model.AppointmentTemplateServiceDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentTemplateMasterServiceImpl implements IAppointmentTemplateMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(AppointmentTemplateMasterServiceImpl.class);

	
	@Autowired
	IAppointmentTemplateMasterDao iAppointmentTemplateMasterDao;
	
	@Autowired
	IAppointmentTemplateMasterMapperDao iAppointmentTemplateMasterMapperDao;
	
	@Autowired
	IAppointmentTemplateServiceDao iAppointmentTemplateServiceDao;

	
	@Override
	public Response saveAppointmentTemplate(AppointmentTemplateMasterDto dto) throws ApplicationException {
		try {
			
			Response res =  iAppointmentTemplateMasterDao.saveAppointmentTemplate(dto);
			
			if(res.getObject()!= null) {
				AppointmentTemplateMaster master = (AppointmentTemplateMaster)res.getObject();
				
				for (AppointmentTemplateMasterMapperDto mapper : dto.getAppointmentTemplateMasterMapperList()) {
					
					mapper.setAppointmentTemplateMasterId(master.getAppointmentTemplateMasterId());
					Response resMapper =  iAppointmentTemplateMasterMapperDao.saveAppointmentTemplateMapper(mapper);
					
					if(resMapper.getObject()!= null) {
						
						AppointmentTemplateMasterMapper masterMapper = (AppointmentTemplateMasterMapper) resMapper.getObject();
						
						if(mapper.getAppointmentTemplateServiceList()!= null && mapper.getAppointmentTemplateServiceList().size()>0) {
							
							for (AppointmentTemplateServiceDto serviceDto : mapper.getAppointmentTemplateServiceList()) {
								
								serviceDto.setAppointmentTemplateMasterMapperId(masterMapper.getAppointmentTemplateMasterMapperId());
								serviceDto.setAppointmentTemplateMasterId(master.getAppointmentTemplateMasterId());
								
								iAppointmentTemplateServiceDao.saveAppointmentTemplateService(serviceDto);
								
							}
						}
						
						
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
	
		} catch (Exception e) {
		logger.error("saveAppointmentTemplate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAppointmentTemplate(AppointmentTemplateMasterDto dto) throws ApplicationException {

		try {
			
			return iAppointmentTemplateMasterDao.getAppointmentTemplate(dto);
	
		} catch (Exception e) {
		logger.error("getAppointmentTemplate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAppointmentTemplateMapperData(AppointmentTemplateMasterDto dto) throws ApplicationException {
		try {
			
			Response res =  iAppointmentTemplateMasterMapperDao.getAppointmentTemplateMapperData(dto);
			
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				List<AppointmentTemplateMasterMapperDto> list= new ArrayList<AppointmentTemplateMasterMapperDto>();
				list.addAll(res.getListObject());
				
				for (AppointmentTemplateMasterMapperDto appointmentTemplateMasterMapperDto : list) {
					
					Response resService =  iAppointmentTemplateServiceDao.getServiceList(appointmentTemplateMasterMapperDto);
					
					if(resService.getListObject()!= null && resService.getListObject().size()>0) {
						List<AppointmentTemplateServiceDto> listService= new ArrayList<AppointmentTemplateServiceDto>();
						listService.addAll(resService.getListObject());
						appointmentTemplateMasterMapperDto.setAppointmentTemplateServiceList(listService);
					}
					
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
			}else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
	
		} catch (Exception e) {
		logger.error("getAppointmentTemplateMapperData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateStatus(AppointmentTemplateMasterDto dto) throws ApplicationException {
		try {
			
			return iAppointmentTemplateMasterDao.updateStatus(dto);
	
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
 