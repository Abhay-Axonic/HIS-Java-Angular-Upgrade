package com.param.global.unit.service;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IOtimsRoleCreationMasterDao;
import com.param.global.unit.dto.OtimsRoleMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OtimsRoleCreationMasterServiceImpl implements IOtimsRoleCreationMasterService , ICommonConstants{
	final static Logger logger = Logger.getLogger(OtimsRoleCreationMasterServiceImpl.class);


	@Autowired
	IOtimsRoleCreationMasterDao iOtimsRoleCreationMasterDao;
	@Autowired
	private IOtimsService iOtimsService;   
	
	@Override
	public Response saveOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			Response res =  iOtimsRoleCreationMasterDao.saveOtimsRoleMaster(otimsRoleMasterDto);
			if(res.getStatus().equals(SUCCESS))
			{
			CommonResponse response = new CommonResponse();
			response.setMessage("RoleMaster Create Successfully");
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("create");
			payload.setData(res.getObject());
			request.setKey(MasterKeyConstants.StaffRole.toString());
			request.setPayload(payload);
			//iOtimsService.publish(request);
			}
			return res;
		} catch (Exception e) {
		logger.error("saveOtimsRoleMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllOtimsRoleMasterList(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			return iOtimsRoleCreationMasterDao.getAllOtimsRoleMasterList(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("getAllOtimsRoleMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			Response res =  iOtimsRoleCreationMasterDao.updateOtimsRoleMaster(otimsRoleMasterDto);
			if(res.getStatus().equals(SUCCESS))
			{
			CommonResponse response = new CommonResponse();
			response.setMessage("RoleMaster Updated Successfully");
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("update");
			payload.setData(res.getObject());
			request.setKey(MasterKeyConstants.StaffRole.toString());
			request.setPayload(payload);
			//iOtimsService.publish(request);
			}
			return res;
		} catch (Exception e) {
		logger.error("updateOtimsRoleMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOtimsRoleMasterStatus(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException {
		try {
			return  iOtimsRoleCreationMasterDao.updateOtimsRoleMasterStatus(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("updateOtimsRoleMasterStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
