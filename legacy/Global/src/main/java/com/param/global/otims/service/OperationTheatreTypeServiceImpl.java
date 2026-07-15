package com.param.global.otims.service;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOperationTheatreTypeDao;
import com.param.global.otims.dto.OperationTheatreTypeDto;
import com.param.otims.model.OperationTheatreType;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OperationTheatreTypeServiceImpl implements IOperationTheatreTypeService, ICommonConstants{
	final static Logger logger = Logger.getLogger(OperationTheatreTypeServiceImpl.class);


	@Autowired
	IOperationTheatreTypeDao iOperationTheatreTypeDao; 
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Override
	public Response saveOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			Response response=iOperationTheatreTypeDao.getOperationTheatreTypeByName(operationTheatreTypeDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0 ){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res= iOperationTheatreTypeDao.saveOperationTheatreType(operationTheatreTypeDto);
				
				if(res.getObject()!=null) {
				
				OperationTheatreType dto = new OperationTheatreType();
				dto = (OperationTheatreType) res.getObject();
				operationTheatreTypeDto.setOperationTheatreTypeId(dto.getOperationTheatreTypeId());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("create");
				payload.setData(operationTheatreTypeDto);
				request.setKey(MasterKeyConstants.Operation_Theatre_Type.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		} catch (Exception e) {
		logger.error("saveOperationTheatreType failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			Response response=iOperationTheatreTypeDao.getOperationTheatreTypeByNameNotId(operationTheatreTypeDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res = iOperationTheatreTypeDao.updateOperationTheatreType(operationTheatreTypeDto);
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(operationTheatreTypeDto);
				request.setKey(MasterKeyConstants.Operation_Theatre_Type.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateOperationTheatreType failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response updateOperationTheatreTypeStatus(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			return iOperationTheatreTypeDao.updateOperationTheatreTypeStatus(operationTheatreTypeDto);
		
			} catch (Exception e) {
			logger.error("updateOperationTheatreTypeStatus failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	
	@Override
	public Response getOperationTheatreTypeById(Integer operationTheatreTypeId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
				return iOperationTheatreTypeDao.getOperationTheatreTypeById(operationTheatreTypeId, orgId, unitId);
			
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeById failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	

	@Override
	public Response getOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			return iOperationTheatreTypeDao.getOperationTheatreTypeList(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getActiveOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			return iOperationTheatreTypeDao.getActiveOperationTheatreTypeList(operationTheatreTypeDto);
		
			} catch (Exception e) {
			logger.error("getActiveOperationTheatreTypeList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreTypeCount(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			return iOperationTheatreTypeDao.getOperationTheatreTypeCount(operationTheatreTypeDto);
		
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeCount failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreTypeByName(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
			return iOperationTheatreTypeDao.getOperationTheatreTypeByName(operationTheatreTypeDto);
		
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeByName failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreTypeByNameNotId(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try {
				return iOperationTheatreTypeDao.getOperationTheatreTypeByNameNotId(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeByNameNotId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}
