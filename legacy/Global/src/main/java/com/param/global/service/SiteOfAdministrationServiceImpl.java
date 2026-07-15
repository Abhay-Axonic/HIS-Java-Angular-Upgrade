package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.SiteOfAdministrationDao;
import com.param.global.dto.SiteOfAdministrationMasterDto;
import com.param.global.model.SiteOfAdministrationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SiteOfAdministrationServiceImpl implements SiteOfAdministrationMasterService,ICommonConstants{
	final static Logger logger = Logger.getLogger(SiteOfAdministrationServiceImpl.class);


	@Autowired
	SiteOfAdministrationDao siteOfAdministrationDao;
	
	@Autowired
	IEmrService EmrService;  

	@Override
	public Response saveSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		Response res=siteOfAdministrationDao.getSiteOfAdministrationMasterByName(siteOfAdministrationMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res1= siteOfAdministrationDao.saveSiteOfAdministrationMaster(siteOfAdministrationMasterDto);
				
				if(res1.getObject() != null) {
					SiteOfAdministrationMaster mstr = (SiteOfAdministrationMaster) res1.getObject();
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(mstr);
					request.setKey(MasterKeyConstants.SiteOfAdministrationMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}

		} catch (Exception e) {
		logger.error("saveSiteOfAdministrationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		Response res=siteOfAdministrationDao.getSiteOfAdministrationMasterByNameNotId(siteOfAdministrationMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				Response res1= siteOfAdministrationDao.updateSiteOfAdministrationMaster(siteOfAdministrationMasterDto);
				
				if(res1.getObject() != null) {
					SiteOfAdministrationMaster mstr = (SiteOfAdministrationMaster) res1.getObject();
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("update");
					payload.setData(mstr);
					request.setKey(MasterKeyConstants.SiteOfAdministrationMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}

		} catch (Exception e) {
		logger.error("updateSiteOfAdministrationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSiteOfAdministrationMasterStatus(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.updateSiteOfAdministrationMasterStatus(siteOfAdministrationMasterDto);
	
	} catch (Exception e) {
	logger.error("updateSiteOfAdministrationMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterById(Integer siteId, Integer organizationId)
			throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterById(siteId, organizationId);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto, Integer organizationId)
			throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterList(siteOfAdministrationMasterDto,organizationId);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterCount(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterCount(siteOfAdministrationMasterDto);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveSiteOfAdministrationMaster(Integer organizationId) throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getActiveSiteOfAdministrationMaster(organizationId);
	
	} catch (Exception e) {
	logger.error("getActiveSiteOfAdministrationMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterByName(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterByName(siteOfAdministrationMasterDto);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterByNameNotId(
			SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException {
		try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterByNameNotId(siteOfAdministrationMasterDto);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterByNameNotId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterSearchList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
     try {
			
			return siteOfAdministrationDao.getSiteOfAdministrationMasterSearchList(siteOfAdministrationMasterDto);
	
	} catch (Exception e) {
	logger.error("getSiteOfAdministrationMasterSearchList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	

}
