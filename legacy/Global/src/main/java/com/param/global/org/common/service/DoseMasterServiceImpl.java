package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IDoseMasterDao;
import com.param.global.org.common.dto.DoseMasterDto;
import com.param.global.org.common.model.DoseMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DoseMasterServiceImpl implements IDoseMasterService,ICommonConstants {
	final static Logger logger = Logger.getLogger(DoseMasterServiceImpl.class);


	@Autowired
	IDoseMasterDao iDoseMasterDao;
	
	@Autowired
	IEmrService EmrService;   
	
	
	@Override
	public Response saveDoseMaster(DoseMasterDto doseMasterDto) throws ApplicationException {
	Response res=iDoseMasterDao.getDoseMasterListByName(doseMasterDto);
		try {
			if(res.getListObject().size()>0&&res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res1= iDoseMasterDao.saveDoseMaster(doseMasterDto);
				if(res1.getObject() != null) {
					DoseMaster mstr = (DoseMaster) res1.getObject();
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(mstr);
					request.setKey(MasterKeyConstants.DoseMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveDoseMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterListById(int doseId, int orgId) throws ApplicationException {
		try {
			return iDoseMasterDao.getDoseMasterListById(doseId, orgId);
		} catch (Exception e) {
		logger.error("getDoseMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterList(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			return iDoseMasterDao.getDoseMasterList(doseMasterDto);
		} catch (Exception e) {
		logger.error("getDoseMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoseMaster(DoseMasterDto doseMasterDto) throws ApplicationException {
		Response res=iDoseMasterDao.getDoseMasterListByNameNotById(doseMasterDto);
		try {
			if(res.getListObject().size()>0&&res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res1= iDoseMasterDao.updateDoseMaster(doseMasterDto);
				
				if(res1.getObject() != null) {
					DoseMaster mstr = (DoseMaster) res1.getObject();
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("update");
					payload.setData(mstr);
					request.setKey(MasterKeyConstants.DoseMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateDoseMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoseMasterStatus(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			return iDoseMasterDao.updateDoseMasterStatus(doseMasterDto);
		} catch (Exception e) {
		logger.error("updateDoseMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterCount(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			return iDoseMasterDao.getDoseMasterCount(doseMasterDto);
		} catch (Exception e) {
		logger.error("getDoseMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDoseMasterList(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			return iDoseMasterDao.getActiveDoseMasterList(doseMasterDto);
		} catch (Exception e) {
		logger.error("getActiveDoseMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
